package single.project.e_commerce.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import single.project.e_commerce.dto.request.CreateOrderRequestDTO;
import single.project.e_commerce.dto.response.OrderResponseDTO;
import single.project.e_commerce.dto.response.PageResponseDTO;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.mappers.OrderDetailMapper;
import single.project.e_commerce.mappers.OrderMapper;
import single.project.e_commerce.models.*;
import single.project.e_commerce.repositories.*;
import single.project.e_commerce.repositories.specifications.SpecificationBuilder;
import single.project.e_commerce.utils.commons.AppConst;
import single.project.e_commerce.utils.commons.GlobalMethod;
import single.project.e_commerce.utils.enums.ErrorCode;
import single.project.e_commerce.utils.enums.OrderStatus;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final OrderDetailMapper orderDetailMapper;

    @Transactional
    public String createOrder(CreateOrderRequestDTO dto) {
        Order order = orderMapper.toOrder(dto);
        String username = GlobalMethod.extractUserFromContext();
        log.info("find user, cartDetails");
        User user = userRepository.findUserWithCartItems(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
        Set<CartDetail> cartDetails = user.getCart().getCartDetails();
        double totalPrice = 0;
        for (CartDetail cartDetail : cartDetails) {
            totalPrice += cartDetail.getPrice() * cartDetail.getQuantity();
        }
        order.setTotalPrice(totalPrice);
        order.setOrderAt(new Date());
        order.setStatus(OrderStatus.UNPAID);
        order.setUser(user);
        log.info("save order");
        // save order
        orderRepository.save(order);

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartDetail cartDetail : cartDetails) {
            orderDetails.add(
                    OrderDetail.builder()
                            .order(order)
                            .price(cartDetail.getPrice())
                            .quantity(cartDetail.getQuantity())
                            .product(cartDetail.getProduct())
                            .build()
            );
        }

        log.info("save all order details");
        // save orderDetail
        orderDetailRepository.saveAll(orderDetails);
        return "create new order successfully";
    }


    public List<OrderResponseDTO> getMyOrders() {
        String username = GlobalMethod.extractUserFromContext();
        List<Order> orders = orderRepository.findAllOrdersByUserUsername(username);
        return orders.stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    public PageResponseDTO<?> getOrdersByAdvancedFilter(Pageable pageable, String[] order, String[] sortBy) {
        SpecificationBuilder<Order> builder = new SpecificationBuilder<>();
        Pattern pattern = Pattern.compile(AppConst.SEARCH_SPEC_OPERATOR);
        Pattern sortPattern = Pattern.compile(AppConst.SORT_BY);

        // build specification
        for (String s : order) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3),
                        matcher.group(4), matcher.group(5), matcher.group(6));
            }
        }
        List<String> authorityList = GlobalMethod.extractAuthorityFromContext();
        // check if current users are not ADMIN, they only can read their orders
        if (authorityList.stream().noneMatch(s -> s.equals("ROLE_ADMIN"))) {
            String username = GlobalMethod.extractUserFromContext();
            builder.with("", "username", ":", username, "", "");
        }
        Specification<Order> specification = builder.build();

        // build sort properties
        List<Sort.Order> sortOrders = new ArrayList<>();
        for (String sb : sortBy) {
            Matcher sortMatcher = sortPattern.matcher(sb);
            if (sortMatcher.find()) {
                String field = sortMatcher.group(1);
                String value = sortMatcher.group(3);
                Sort.Direction direction = (value.equalsIgnoreCase("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
                sortOrders.add(new Sort.Order(direction, field));
            }
        }
        Sort sort = Sort.by(sortOrders);

        // add sort properties to pageable
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                sort);

        //get page response when not fetching collection yet
        Page<Order> orders = orderRepository.findAll(specification, pageable);
        List<Long> orderedOrderIds = orders.stream().map(Order::getId).toList();
        // fetching collection in the second query
        List<Order> afterFetchedOrders = orderRepository.subPaginationOrder(orderedOrderIds);
        Map<Long, Order> orderByIdMap = afterFetchedOrders.stream()
                .collect(Collectors.toMap(Order::getId, Function.identity()));

        List<OrderResponseDTO> result = orderedOrderIds.stream()
                .map(orderByIdMap::get)
                .map(orderMapper::toResponse)
                .toList();
        return PageResponseDTO.builder()
                .data(result)
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(orders.getTotalPages())
                .build();
    }

    public String cancelOrder(long id) {
        String username = GlobalMethod.extractUserFromContext();
        Order order = orderRepository.findOrderByUserUsernameAndId(username, id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXIST));
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        return "cancelled order with id " + id;
    }
}

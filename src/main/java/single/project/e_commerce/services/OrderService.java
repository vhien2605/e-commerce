package single.project.e_commerce.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import single.project.e_commerce.dto.request.CreateOrderRequestDTO;
import single.project.e_commerce.dto.response.OrderDetailResponseDTO;
import single.project.e_commerce.dto.response.OrderResponseDTO;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.mappers.OrderDetailMapper;
import single.project.e_commerce.mappers.OrderMapper;
import single.project.e_commerce.models.*;
import single.project.e_commerce.repositories.*;
import single.project.e_commerce.utils.commons.GlobalMethod;
import single.project.e_commerce.utils.enums.ErrorCode;
import single.project.e_commerce.utils.enums.OrderStatus;

import java.util.*;

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
}

package single.project.e_commerce.services;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import single.project.e_commerce.dto.request.UserRequestDTO;
import single.project.e_commerce.dto.request.UserUpdateRequestDTO;
import single.project.e_commerce.dto.response.PageResponseDTO;
import single.project.e_commerce.dto.response.UserResponseDTO;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.mappers.AddressMapper;
import single.project.e_commerce.mappers.UserMapper;
import single.project.e_commerce.models.Address;
import single.project.e_commerce.models.Cart;
import single.project.e_commerce.models.Role;
import single.project.e_commerce.models.User;
import single.project.e_commerce.repositories.AddressRepository;
import single.project.e_commerce.repositories.RoleRepository;
import single.project.e_commerce.repositories.UserRepository;
import single.project.e_commerce.repositories.specifications.SpecificationBuilder;
import single.project.e_commerce.utils.commons.AppConst;
import single.project.e_commerce.utils.enums.ErrorCode;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public UserResponseDTO saveUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByUsername(userRequestDTO.getUsername())
                || userRepository.existsByEmail(userRequestDTO.getEmail())
        ) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(userRequestDTO);


        // set address if exist or create new Address
        var address = addressRepository.getAddressByLocation(user.getAddress().getName()
                , user.getAddress().getCity(), user.getAddress().getCountry());
        address.ifPresent(user::setAddress);

        //set default role
        Role defaultRole = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        user.setRoles(roles);


        //set cart
        Cart cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);

        //set password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return userMapper.toResponse(user);
    }


    public UserResponseDTO updateUser(Long userId, UserUpdateRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        // set normal fields
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setGender(request.getGender());
        user.setStatus(request.getStatus());
        user.setAddress(addressMapper.toAddress(request.getAddress()));

        //set address
        var address = addressRepository.getAddressByLocation(request.getAddress().getName()
                , request.getAddress().getCity(), request.getAddress().getCountry());
        address.ifPresent(user::setAddress);

        //set roles
        Set<Role> roles = new HashSet<>(roleRepository.findAllByNameIn(request.getRoles()));
        user.setRoles(roles);

        //save user
        userRepository.save(user);
        return userMapper.toResponse(user);
    }


    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAllUsersWithAllReferences();
        return users
                .stream().map(userMapper::toResponse).toList();
    }

    public List<UserResponseDTO> getAllUsersAdvancedFilter(String[] user, String[] sortBy) {
        SpecificationBuilder<User> builder = new SpecificationBuilder<>();
        Pattern pattern = Pattern.compile(AppConst.SEARCH_SPEC_OPERATOR);
        Pattern sortPattern = Pattern.compile(AppConst.SORT_BY);
        for (String s : user) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3),
                        matcher.group(4), matcher.group(5), matcher.group(6));
            }
        }
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
        Specification<User> specification = builder.build();
        return userRepository.findAll(specification, sort).stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public PageResponseDTO<?> getAllUsersAdvancedFilterAndPagination(Pageable pageable, String[] user, String[] sortBy) {
        SpecificationBuilder<User> builder = new SpecificationBuilder<>();
        Pattern pattern = Pattern.compile(AppConst.SEARCH_SPEC_OPERATOR);
        Pattern sortPattern = Pattern.compile(AppConst.SORT_BY);
        for (String s : user) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3),
                        matcher.group(4), matcher.group(5), matcher.group(6));
            }
        }
        Specification<User> specification = builder.build();


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

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                sort);

        //get page response when not fetching collection yet
        Page<User> users = userRepository.findAll(specification, sortedPageable);
        List<Long> orderedIds = users.stream().map(User::getId).toList();
        // fetching collection in the second query
        List<User> afterFetchedUsers = userRepository.findAllUsersWithId(orderedIds);
        Map<Long, User> userByIdMap = afterFetchedUsers.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        List<UserResponseDTO> result = orderedIds.stream()
                .map(userByIdMap::get)
                .map(userMapper::toResponse)
                .toList();
        return PageResponseDTO.builder()
                .data(result)
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(users.getTotalPages())
                .build();
    }


    public String changeUserPassword(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
        if (passwordEncoder.matches(password, user.getPassword())) {
            throw new AppException(ErrorCode.NEW_PASSWORD_EXISTED);
        }
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return "changed password successfully!";
    }
}
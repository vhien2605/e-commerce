package single.project.e_commerce.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import single.project.e_commerce.dto.request.UserRequestDTO;
import single.project.e_commerce.dto.request.UserUpdateRequestDTO;
import single.project.e_commerce.dto.response.UserResponseDTO;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.mappers.AddressMapper;
import single.project.e_commerce.mappers.UserMapper;
import single.project.e_commerce.models.Role;
import single.project.e_commerce.models.User;
import single.project.e_commerce.repositories.AddressRepository;
import single.project.e_commerce.repositories.RoleRepository;
import single.project.e_commerce.repositories.UserRepository;
import single.project.e_commerce.utils.enums.ErrorCode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public User getUserWithRolesAndPermissions(long userId) {
        return userRepository.findUserWithRoleAndPermission(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
    }


    public UserResponseDTO saveUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
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
        return userRepository.findAllUsersWithRolesAndAddress()
                .stream().map(userMapper::toResponse).toList();
    }
}
package single.project.e_commerce.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import single.project.e_commerce.dto.request.UserRequestDTO;
import single.project.e_commerce.dto.response.UserResponseDTO;
import single.project.e_commerce.exceptions.DataDuplicateException;
import single.project.e_commerce.exceptions.DataInvalidException;
import single.project.e_commerce.mappers.RoleMapper;
import single.project.e_commerce.mappers.UserMapper;
import single.project.e_commerce.models.Address;
import single.project.e_commerce.models.Role;
import single.project.e_commerce.models.User;
import single.project.e_commerce.repositories.AddressRepository;
import single.project.e_commerce.repositories.RoleRepository;
import single.project.e_commerce.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    public User getUserWithRolesAndPermissions(long userId) {
        return userRepository.findUserWithRoleAndPermission(userId)
                .orElseThrow(() -> new DataInvalidException("Can't find user!"));
    }


    public UserResponseDTO saveUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new DataDuplicateException("Username is exist!");
        }
        User user = userMapper.toUser(userRequestDTO);

        var address = addressRepository.getAddressByLocation(user.getAddress().getName()
                , user.getAddress().getCity(), user.getAddress().getCountry());
        address.ifPresent(user::setAddress);

        Role defaultRole = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new DataInvalidException("Can't find role with suggested name"));
        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        user.setRoles(roles);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return userMapper.toResponse(user);
    }


    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAllUsersWithRolesAndAddress()
                .stream().map(userMapper::toResponse).toList();
    }
}
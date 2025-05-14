package single.project.e_commerce.services;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import single.project.e_commerce.dto.request.AddressRequestDTO;
import single.project.e_commerce.dto.request.UserRequestDTO;
import single.project.e_commerce.dto.request.UserUpdateRequestDTO;
import single.project.e_commerce.dto.response.UserResponseDTO;
import single.project.e_commerce.mappers.UserMapper;
import single.project.e_commerce.models.*;
import single.project.e_commerce.repositories.AddressRepository;
import single.project.e_commerce.repositories.RoleRepository;
import single.project.e_commerce.repositories.UserRepository;
import single.project.e_commerce.repositories.specifications.GenericSpecification;
import single.project.e_commerce.repositories.specifications.SpecificationBuilder;
import single.project.e_commerce.utils.commons.AppConst;
import single.project.e_commerce.utils.enums.Gender;
import single.project.e_commerce.utils.enums.SearchOperation;
import single.project.e_commerce.utils.enums.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @MockitoBean
    private RoleRepository roleRepository;


    @MockitoBean
    private AddressRepository addressRepository;


    @BeforeEach
    public void init() {
        User user = User.builder()
                .id(1)
                .username("hvu6582")
                .password("deptrailoitaiai")
                .status(Status.ACTIVE)
                .email("hvu6582@gmail.com")
                .gender(Gender.MALE)
                .fullName("vu minh hien")
                .build();
        Address address = Address.builder()
                .id(1)
                .city("nam dinh")
                .country("viet nam")
                .name("trung dong")
                .build();
        user.setAddress(address);
        userRepository.save(user);
    }

    @Test
    public void getAllUsersTest() {
        List<User> users = List.of(
                User.builder()
                        .username("hvu6582")
                        .password("deptrailoitaiai")
                        .status(Status.ACTIVE)
                        .email("hvu6582@gmail.com")
                        .gender(Gender.MALE)
                        .fullName("vu minh hien")
                        .build(),
                User.builder()
                        .username("hvu123")
                        .password("deptrailoitaiai")
                        .status(Status.ACTIVE)
                        .email("hvu123@gmail.com")
                        .gender(Gender.MALE)
                        .fullName("tran van minh")
                        .build()
        );
        Mockito.when(userRepository.findAllUsersWithAllReferences()).thenReturn(users);
        List<UserResponseDTO> result = userService.getAllUsers();
        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void addUserTest() {
        UserRequestDTO dto = UserRequestDTO.builder()
                .fullName("Tran minh hien")
                .email("asdad@gmail.com")
                .username("testing")
                .password("deptrai")
                .gender(Gender.MALE)
                .status(Status.ACTIVE)
                .address(AddressRequestDTO.builder()
                        .name("trung dong")
                        .city("nam dinh")
                        .country("viet nam")
                        .build())
                .build();


        Permission permission = Permission.builder()
                .name("read_product")
                .build();
        Role role = Role.builder()
                .name("CUSTOMER")
                .permissions(Set.of(permission))
                .build();


        Mockito.when(roleRepository.findByName("CUSTOMER")).thenReturn(
                Optional.of(role)
        );


        UserResponseDTO responseDTO = userService.saveUser(dto);
        Assertions.assertEquals("Tran minh hien", responseDTO.getFullName());
        Assertions.assertEquals("asdad@gmail.com", responseDTO.getEmail());
    }

    @Test
    public void updateUserTest() {
        long userId = 1;
        Permission permission = Permission.builder()
                .name("read_product")
                .build();
        Role role = Role.builder()
                .name("CUSTOMER")
                .permissions(Set.of(permission))
                .build();

        Mockito.when(addressRepository.getAddressByLocation("trung dong", "nam dinh", "viet nam"))
                .thenReturn(Optional.of(Address.builder()
                        .name("trung dong")
                        .country("viet nam")
                        .city("nam dinh")
                        .build()));

        Mockito.when(userRepository.findById(1L)).thenReturn(
                Optional.of(User.builder()
                        .fullName("Tran minh coi")
                        .email("asdad@gmail.com")
                        .username("testing")
                        .password("deptrai")
                        .gender(Gender.MALE)
                        .status(Status.ACTIVE)
                        .roles(Set.of(role))
                        .address(Address.builder()
                                .name("trung dong")
                                .city("nam dinh")
                                .country("viet nam")
                                .build())
                        .build())
        );

        Mockito.when(roleRepository.findAllByNameIn(List.of("CUSTOMER")))
                .thenReturn(List.of(role));


        UserUpdateRequestDTO dto = UserUpdateRequestDTO.builder()
                .fullName("Tran minh coi")
                .email("asdad@gmail.com")
                .username("testing")
                .password("deptrai")
                .gender(Gender.MALE)
                .status(Status.ACTIVE)
                .roles(Set.of("CUSTOMER"))
                .address(AddressRequestDTO.builder()
                        .name("trung dong")
                        .city("nam dinh")
                        .country("viet nam")
                        .build())
                .build();

        UserResponseDTO responseDTO = userService.updateUser(userId, dto);
        Assertions.assertEquals("Tran minh coi", responseDTO.getFullName());
        Assertions.assertEquals("asdad@gmail.com", responseDTO.getEmail());
    }


    @Test
    public void getAllUsersAdvancedFilterTest() {
        Permission permission = Permission.builder()
                .name("read_product")
                .build();
        Role role = Role.builder()
                .name("CUSTOMER")
                .permissions(Set.of(permission))
                .build();

        String[] user = {"username:testing"};
        String[] sortBy = {"id:asc"};
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


        Mockito.when(userRepository.findAll(Mockito.any(Specification.class), Mockito.any(Sort.class)))
                .thenReturn(List.of(
                        User.builder()
                                .fullName("Tran minh coi")
                                .email("asdad@gmail.com")
                                .username("testing")
                                .password("deptrai")
                                .gender(Gender.MALE)
                                .status(Status.ACTIVE)
                                .roles(Set.of(role))
                                .address(Address.builder()
                                        .name("trung dong")
                                        .city("nam dinh")
                                        .country("viet nam")
                                        .build())
                                .build())
                );

        List<UserResponseDTO> result = userService.getAllUsersAdvancedFilter(user, sortBy);
        Assertions.assertEquals("Tran minh coi", result.get(0).getFullName());
        GenericSpecification<User> genericSpecification = (GenericSpecification<User>) specification;
        Assertions.assertEquals("username", genericSpecification.getCriteria().getKey());
        Assertions.assertEquals(SearchOperation.EQUALITY, genericSpecification.getCriteria().getOperation());
        Assertions.assertEquals("testing", genericSpecification.getCriteria().getValue());
        Assertions.assertEquals("id: ASC", sort.toString());
    }
}

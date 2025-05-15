package single.project.e_commerce.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import single.project.e_commerce.configuration.securityModels.SecurityUser;
import single.project.e_commerce.dto.request.AddressRequestDTO;
import single.project.e_commerce.dto.request.UserRequestDTO;
import single.project.e_commerce.dto.response.UserResponseDTO;
import single.project.e_commerce.models.Permission;
import single.project.e_commerce.models.Role;
import single.project.e_commerce.models.User;
import single.project.e_commerce.services.AuthenticationService;
import single.project.e_commerce.services.JwtService;
import single.project.e_commerce.services.UserService;
import single.project.e_commerce.utils.enums.Gender;
import single.project.e_commerce.utils.enums.Status;
import single.project.e_commerce.utils.enums.TokenType;

import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;


    @Test
    @WithMockUser(username = "testing", roles = "SYSTEM_ADMIN")
    public void createUserTest() throws Exception {
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

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(dto);

        Mockito.when(userService.saveUser(Mockito.any(UserRequestDTO.class)))
                .thenReturn(
                        UserResponseDTO.builder()
                                .id(1)
                                .fullName("Tran minh hien")
                                .email("asdad@gmail.com")
                                .username("testing")
                                .gender(Gender.MALE)
                                .status(Status.ACTIVE)
                                .build()
                );
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/user/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("status")
                        .value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("data.fullName")
                        .value("Tran minh hien"));
    }

    @Test
    @WithMockUser(username = "testing", roles = "CUSTOMER")
    public void createUserPermissionNotAllowedTest() throws Exception {
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

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(dto);

        Mockito.when(userService.saveUser(dto))
                .thenReturn(
                        UserResponseDTO.builder()
                                .fullName("Tran minh hien")
                                .email("asdad@gmail.com")
                                .username("testing")
                                .gender(Gender.MALE)
                                .status(Status.ACTIVE)
                                .build()
                );

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/user/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("status")
                        .value(403));
    }


    @Test
    public void unauthorizedCreateUserTest() throws Exception {
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

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(dto);

        Mockito.when(userService.saveUser(dto))
                .thenReturn(
                        UserResponseDTO.builder()
                                .fullName("Tran minh hien")
                                .email("asdad@gmail.com")
                                .username("testing")
                                .gender(Gender.MALE)
                                .status(Status.ACTIVE)
                                .build()
                );

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/user/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("status")
                        .value(401));
    }
}

package single.project.e_commerce.services;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;
import single.project.e_commerce.dto.request.CategoryRequestDTO;
import single.project.e_commerce.dto.request.ProductRequestDTO;
import single.project.e_commerce.dto.response.ProductResponseDTO;
import single.project.e_commerce.models.*;
import single.project.e_commerce.repositories.CategoryRepository;
import single.project.e_commerce.repositories.UserRepository;
import single.project.e_commerce.utils.commons.GlobalMethod;
import single.project.e_commerce.utils.enums.Gender;
import single.project.e_commerce.utils.enums.Status;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockitoBean
    private FileService fileService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private CategoryRepository categoryRepository;

    @Test
    public void saveProductTest() throws IOException {
        try (MockedStatic<GlobalMethod> mockedStatic = Mockito.mockStatic(GlobalMethod.class)) {
            // given
            mockedStatic.when(GlobalMethod::extractUserFromContext).thenReturn("testing");
            ProductRequestDTO requestDTO = ProductRequestDTO.builder()
                    .name("ao hoodie")
                    .longDesc("ao dep")
                    .shortDesc("ao dep")
                    .remainingQuantity(100)
                    .price(100000)
                    .categories(
                            Set.of(CategoryRequestDTO.builder()
                                    .name("thoi trang")
                                    .description("thoi trang")
                                    .build())
                    )
                    .build();

            Permission permission = Permission.builder()
                    .name("read_product")
                    .build();
            Role role = Role.builder()
                    .name("CUSTOMER")
                    .permissions(Set.of(permission))
                    .build();

            MockMultipartFile mockFile = new MockMultipartFile(
                    "file",
                    "test.txt",
                    "text/plain",
                    "Hello, world!".getBytes()
            );

            //when,then

            Mockito.when(userRepository.findByUsername("testing"))
                    .thenReturn(Optional.of(User.builder()
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
                            .build()));

            Mockito.when(categoryRepository.findAll())
                    .thenReturn(
                            List.of()
                    );

            Mockito.when(fileService.uploadFile(Mockito.any(MultipartFile.class), Mockito.any(String.class)))
                    .thenReturn("http://cloudinary/products/1");

            // checking
            ProductResponseDTO responseDTO = productService.save(requestDTO, mockFile);
            Assertions.assertEquals("ao hoodie", responseDTO.getName());
            Assertions.assertEquals("http://cloudinary/products/1", responseDTO.getImageUrl());
        }
    }


    @Test
    public void updateProductTest() {

    }

    @Test
    public void getProductFilter() {

    }

    @Test
    public void getAllProductFilterPagination() {

    }
}

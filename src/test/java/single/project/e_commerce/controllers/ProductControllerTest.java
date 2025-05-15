package single.project.e_commerce.controllers;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;
import single.project.e_commerce.dto.request.ProductRequestDTO;
import single.project.e_commerce.dto.response.ProductResponseDTO;
import single.project.e_commerce.models.Product;
import single.project.e_commerce.services.ProductService;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    @WithMockUser(username = "testing", authorities = {"create_product"})
    public void createProductControllerTest() throws Exception {
        String data = "{\n" +
                "    \"name\":\"Áo hoodie nam2\",\n" +
                "    \"price\":170000,\n" +
                "    \"shortDesc\":\"Áo hoodie cộc tay cho nam mạnh mẽ222\",\n" +
                "    \"longDesc\":\"Sản phẩm áo hoodle hàng Quảng Châu, chất vải thoáng mát, dành cho teen nữ theo đuổi phong cách mạnh mẽ\",\n" +
                "    \"remainingQuantity\":1500,\n" +
                "    \"categories\":[\n" +
                "        {\n" +
                "            \"name\":\"Thời trang\",\n" +
                "            \"description\":\"Áo hoodie\"\n" +
                "        }\n" +
                "    ]\n" +
                "}\n";
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes()
        );
        Mockito.when(productService.save(Mockito.any(ProductRequestDTO.class), Mockito.any(MultipartFile.class)))
                .thenReturn(
                        ProductResponseDTO.builder()
                                .id(1)
                                .name("ao hoodie")
                                .price(1000000)
                                .build());


        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/api/product/")
                        .file(file)
                        .param("data", data)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("status")
                        .value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("data.name")
                        .value("ao hoodie"));
    }


    @Test
    @WithMockUser(username = "testing", authorities = {"create_product"})
    public void createProductControllerJsonInvalidTest() throws Exception {
        String data = "{\n" +
                "    \"naÁo hoodie nam2\",\n" +
                "    \"price\":170000,\n" +
                "    \"shortDesc\":\"Áo hoodie cộc tay cho nam mạnh mẽ222\",\n" +
                "    \"longDesc\":\"Sản phẩm áo hoodle hàng Quảng Châu, chất vải thoáng mát, dành cho teen nữ theo đuổi phong cách mạnh mẽ\",\n" +
                "    \"remainingQuantity\":1500,\n" +
                "    \"categories\":[\n" +
                "        {\n" +
                "            \"name\":\"Thời trang\",\n" +
                "            \"description\":\"Áo hoodie\"\n" +
                "        }\n" +
                "    ]\n" +
                "}\n";
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes()
        );
        Mockito.when(productService.save(Mockito.any(ProductRequestDTO.class), Mockito.any(MultipartFile.class)))
                .thenReturn(
                        ProductResponseDTO.builder()
                                .id(1)
                                .name("ao hoodie")
                                .price(1000000)
                                .build());


        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/api/product/")
                        .file(file)
                        .param("data", data)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("status")
                        .value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("input json is invalid"));
    }
}

package single.project.e_commerce.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import single.project.e_commerce.dto.request.ProductRequestDTO;
import single.project.e_commerce.dto.response.ApiResponse;
import single.project.e_commerce.dto.response.ApiSuccessResponse;
import single.project.e_commerce.dto.response.ProductResponseDTO;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.services.ProductService;
import single.project.e_commerce.utils.enums.ErrorCode;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@Validated
public class ProductController {
    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @PostMapping("/")
    public ApiResponse createProduct(@RequestParam("data") String data,
                                     @RequestParam("file") MultipartFile file
    ) {
        try {
            ProductRequestDTO dto = objectMapper.readValue(data, ProductRequestDTO.class);
            return ApiSuccessResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Create product successfully")
                    .data(productService.save(dto, file))
                    .build();
        } catch (JsonProcessingException e) {
            throw new AppException(ErrorCode.JSON_INVALID);
        }
    }
}

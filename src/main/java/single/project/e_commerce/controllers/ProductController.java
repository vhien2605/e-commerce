package single.project.e_commerce.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import single.project.e_commerce.dto.request.ProductRequestDTO;
import single.project.e_commerce.dto.request.ProductUpdateRequestDTO;
import single.project.e_commerce.dto.response.ApiResponse;
import single.project.e_commerce.dto.response.ApiSuccessResponse;
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
    @PreAuthorize("hasAuthority('create_product')")
    public ApiResponse createProduct(@RequestParam("data") String data,
                                     @RequestParam("file") MultipartFile file) {
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


    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('create_product')")
    public ApiResponse updateProduct(@PathVariable("id") Long id,
                                     @RequestParam("data") String data,
                                     @RequestParam("file") MultipartFile file) {
        try {
            ProductUpdateRequestDTO dto = objectMapper.readValue(data, ProductUpdateRequestDTO.class);
            return ApiSuccessResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Update product successfully")
                    .data(productService.updateProduct(id, dto, file))
                    .build();
        } catch (JsonProcessingException e) {
            throw new AppException(ErrorCode.JSON_INVALID);
        }
    }


    @GetMapping("/")
    @PreAuthorize("hasAuthority('read_product')")
    public ApiResponse getAllProducts() {
        return ApiSuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("read product successfully")
                .data(productService.getAllProducts())
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read_product')")
    public ApiResponse productDetail(@PathVariable("id") Long id) {
        return ApiSuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("read detail information product successfully")
                .data(productService.productDetail(id))
                .build();
    }
}

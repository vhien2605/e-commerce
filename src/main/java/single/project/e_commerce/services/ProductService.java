package single.project.e_commerce.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import single.project.e_commerce.dto.request.ProductRequestDTO;
import single.project.e_commerce.dto.response.ProductResponseDTO;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.mappers.ProductMapper;
import single.project.e_commerce.models.Product;
import single.project.e_commerce.repositories.ProductRepository;
import single.project.e_commerce.utils.enums.ErrorCode;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final FileService fileService;

    public ProductResponseDTO save(ProductRequestDTO dto, MultipartFile file) {
        Product product = productMapper.toProduct(dto);
        try {
            String fileUrl = fileService.uploadFile(file, "product_images");
            product.setImageUrl(fileUrl);
            product.setSoldQuantity(0);
            return productMapper.toResponse(productRepository.save(product));
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_IO_ERROR);
        }
    }
}

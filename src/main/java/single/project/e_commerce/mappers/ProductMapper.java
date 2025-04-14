package single.project.e_commerce.mappers;

import org.mapstruct.Mapper;
import single.project.e_commerce.dto.request.ProductRequestDTO;
import single.project.e_commerce.dto.response.ProductResponseDTO;
import single.project.e_commerce.models.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductRequestDTO dto);

    ProductResponseDTO toResponse(Product product);
}

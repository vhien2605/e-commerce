package single.project.e_commerce.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import single.project.e_commerce.dto.response.CartItemResponseDTO;
import single.project.e_commerce.models.CartDetail;

@Mapper(componentModel = "spring")
public interface CartDetailMapper {
    @Mapping(source = "product.name", target = "name")
    @Mapping(source = "product.imageUrl", target = "imageUrl")
    CartItemResponseDTO toResponse(CartDetail cartDetail);
}

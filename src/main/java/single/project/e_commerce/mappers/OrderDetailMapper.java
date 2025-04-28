package single.project.e_commerce.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import single.project.e_commerce.dto.response.OrderDetailResponseDTO;
import single.project.e_commerce.models.OrderDetail;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    @Mapping(source = "product.name", target = "productId")
    @Mapping(source = "product.name", target = "name")
    @Mapping(source = "product.imageUrl", target = "imageUrl")
    OrderDetailResponseDTO toResponse(OrderDetail orderDetail);
}

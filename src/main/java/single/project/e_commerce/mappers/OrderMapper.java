package single.project.e_commerce.mappers;

import org.mapstruct.Mapper;
import single.project.e_commerce.dto.request.CreateOrderRequestDTO;
import single.project.e_commerce.dto.response.OrderResponseDTO;
import single.project.e_commerce.models.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toOrder(CreateOrderRequestDTO dto);

    OrderResponseDTO toResponse(Order order);
}

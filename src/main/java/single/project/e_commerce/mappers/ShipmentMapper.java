package single.project.e_commerce.mappers;

import org.mapstruct.Mapper;
import single.project.e_commerce.dto.response.ShipmentResponseDTO;
import single.project.e_commerce.models.Shipment;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {
    ShipmentResponseDTO toResponse(Shipment shipment);
}

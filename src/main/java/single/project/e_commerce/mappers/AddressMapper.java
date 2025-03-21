package single.project.e_commerce.mappers;


import org.mapstruct.Mapper;
import single.project.e_commerce.dto.request.AddressRequestDTO;
import single.project.e_commerce.dto.response.AddressResponseDTO;
import single.project.e_commerce.models.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toAddress(AddressRequestDTO dto);

    AddressResponseDTO toResponse(Address address);
}

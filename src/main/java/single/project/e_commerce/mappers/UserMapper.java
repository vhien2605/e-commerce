package single.project.e_commerce.mappers;


import org.mapstruct.Mapper;
import single.project.e_commerce.dto.request.UserRequestDTO;
import single.project.e_commerce.dto.response.OwnedShopInformationDTO;
import single.project.e_commerce.dto.response.UserResponseDTO;
import single.project.e_commerce.models.User;

@Mapper(componentModel = "spring", uses = {RoleMapper.class, AddressMapper.class})
public interface UserMapper {
    User toUser(UserRequestDTO dto);

    UserResponseDTO toResponse(User user);

    OwnedShopInformationDTO toShopResponse(User user);
}

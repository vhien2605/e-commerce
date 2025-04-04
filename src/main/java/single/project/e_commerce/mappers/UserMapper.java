package single.project.e_commerce.mappers;


import org.mapstruct.Mapper;
import single.project.e_commerce.dto.request.UserRequestDTO;
import single.project.e_commerce.dto.request.UserUpdateRequestDTO;
import single.project.e_commerce.dto.response.UserResponseDTO;
import single.project.e_commerce.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequestDTO dto);

    UserResponseDTO toResponse(User user);
    
}

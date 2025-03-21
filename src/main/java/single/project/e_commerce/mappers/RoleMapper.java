package single.project.e_commerce.mappers;


import org.mapstruct.Mapper;
import single.project.e_commerce.dto.response.RoleResponseDTO;
import single.project.e_commerce.models.Role;

@Mapper(componentModel = "string")
public interface RoleMapper {
    RoleResponseDTO toResponse(Role role);
}

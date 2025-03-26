package single.project.e_commerce.mappers;


import org.mapstruct.Mapper;
import single.project.e_commerce.dto.request.PermissionRequestDTO;
import single.project.e_commerce.dto.response.PermissionResponseDTO;
import single.project.e_commerce.models.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequestDTO dto);

    PermissionResponseDTO toResponse(Permission permission);
}

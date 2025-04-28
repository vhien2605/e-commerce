package single.project.e_commerce.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import single.project.e_commerce.dto.request.RoleRequestDTO;
import single.project.e_commerce.dto.response.RoleResponseDTO;
import single.project.e_commerce.models.Permission;
import single.project.e_commerce.models.Role;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {
    @Mapping(target = "permissions", source = "permissions", qualifiedByName = "mapPermissions")
    Role toRole(RoleRequestDTO dto);

    RoleResponseDTO toResponse(Role role);

    @Named("mapPermissions")
    default Set<Permission> mapPermissions(Set<String> permissionNames) {
        return permissionNames.stream()
                .map(s -> Permission.builder().name(s).build())
                .collect(Collectors.toSet());
    }
}

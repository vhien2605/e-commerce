package single.project.e_commerce.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import single.project.e_commerce.dto.request.RoleRequestDTO;
import single.project.e_commerce.dto.request.ShopRequestDTO;
import single.project.e_commerce.dto.response.RoleResponseDTO;
import single.project.e_commerce.dto.response.UserResponseDTO;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.mappers.RoleMapper;
import single.project.e_commerce.mappers.ShopMapper;
import single.project.e_commerce.mappers.UserMapper;
import single.project.e_commerce.models.Permission;
import single.project.e_commerce.models.Role;
import single.project.e_commerce.models.Shop;
import single.project.e_commerce.models.User;
import single.project.e_commerce.repositories.PermissionRepository;
import single.project.e_commerce.repositories.RoleRepository;
import single.project.e_commerce.repositories.ShopRepository;
import single.project.e_commerce.repositories.UserRepository;
import single.project.e_commerce.utils.commons.GlobalMethod;
import single.project.e_commerce.utils.enums.ErrorCode;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    public RoleResponseDTO createRole(RoleRequestDTO requestDTO) {
        if (roleRepository.existsByName(requestDTO.getName())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }

        // find existed permissions
        Role role = roleMapper.toRole(requestDTO);

        // find permissions if exist
        List<Permission> permissions = permissionRepository.findAllByNameIn(requestDTO.getPermissions().stream().toList());
        List<String> existedPermissionNames = permissions.stream().map(Permission::getName).toList();
        permissions.addAll(requestDTO.getPermissions().stream()
                .filter(s -> !existedPermissionNames.contains(s))
                .map(s -> Permission.builder().name(s).build()).toList());
        role.setPermissions(new HashSet<>(permissions));

        // save role
        roleRepository.save(role);
        return roleMapper.toResponse(role);
    }


    public RoleResponseDTO updateRole(Long roleId, RoleRequestDTO dto) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        // find permissions if exist
        List<Permission> permissions = permissionRepository.findAllByNameIn(dto.getPermissions().stream().toList());
        List<String> existedPermissionNames = permissions.stream().map(Permission::getName).toList();
        permissions.addAll(dto.getPermissions().stream()
                .filter(s -> !existedPermissionNames.contains(s))
                .map(s -> Permission.builder().name(s).build()).toList());


        //setter and save
        role.setPermissions(new HashSet<>(permissions));
        roleRepository.save(role);
        return roleMapper.toResponse(role);
    }


    public List<RoleResponseDTO> getAllRolesWithPermissions() {
        return roleRepository.findAllRolesWithPermissions().stream()
                .map(roleMapper::toResponse)
                .toList();
    }


}

package single.project.e_commerce.services;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import single.project.e_commerce.dto.request.RoleRequestDTO;
import single.project.e_commerce.dto.response.RoleResponseDTO;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.models.Permission;
import single.project.e_commerce.models.Role;
import single.project.e_commerce.repositories.PermissionRepository;
import single.project.e_commerce.repositories.RoleRepository;
import single.project.e_commerce.utils.enums.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @MockitoBean
    private RoleRepository roleRepository;

    @MockitoBean
    private PermissionRepository permissionRepository;

    @Test
    public void createRoleTest() {
        Set<String> permissions = Set.of("create_role");
        RoleRequestDTO requestDTO = RoleRequestDTO.builder()
                .name("ADMIN")
                .permissions(permissions)
                .build();

        Mockito.when(roleRepository.existsByName("ADMIN"))
                .thenReturn(false);

        List<Permission> mockPermissions = new ArrayList<>();
        mockPermissions.add(Permission.builder().id(1).name("create_role").build());

        Mockito.when(permissionRepository.findAllByNameIn(Mockito.anyList()))
                .thenReturn(mockPermissions);

        roleService.createRole(requestDTO);
    }


    @Test
    public void createRoleExceptionTest() {
        Set<String> permissions = Set.of("create_role");
        RoleRequestDTO requestDTO = RoleRequestDTO.builder()
                .name("ADMIN")
                .permissions(permissions)
                .build();

        Mockito.when(roleRepository.existsByName("ADMIN"))
                .thenReturn(true);

        Assertions.assertThrows(AppException.class, () -> roleService.createRole(requestDTO));
    }


    @Test
    public void updateRoleTest() {
        long roleId = 1;
        Set<String> permissions = Set.of("create_role", "update_role");
        RoleRequestDTO requestDTO = RoleRequestDTO.builder()
                .name("DEP_TRAI")
                .permissions(permissions)
                .build();


        Mockito.when(roleRepository.findById(roleId))
                .thenReturn(
                        Optional.of(Role.builder()
                                .id(1)
                                .name("ADMIN")
                                .build()));

        List<Permission> mockPermissions = new ArrayList<>();
        mockPermissions.add(Permission.builder().id(1).name("create_role").build());

        Mockito.when(permissionRepository.findAllByNameIn(Mockito.anyList()))
                .thenReturn(mockPermissions);

        RoleResponseDTO responseDTO = roleService.updateRole(roleId, requestDTO);
        Assertions.assertEquals("ADMIN", responseDTO.getName());
        Assertions.assertEquals(2, responseDTO.getPermissions().size());
    }

    @Test
    public void updateRoleException() {
        long roleId = 1;
        Set<String> permissions = Set.of("create_role", "update_role");
        RoleRequestDTO requestDTO = RoleRequestDTO.builder()
                .name("DEP_TRAI")
                .permissions(permissions)
                .build();


        Mockito.when(roleRepository.findById(roleId))
                .thenThrow(new AppException(ErrorCode.ROLE_NOT_EXISTED));
        Assertions.assertThrows(AppException.class, () -> roleService.updateRole(roleId, requestDTO));
    }

    @Test
    public void getAllRoleWithPermission() {
        Set<Permission> permissions = Set.of(Permission.builder()
                .name("create_role")
                .build());
        Mockito.when(roleRepository.findAllRolesWithPermissions())
                .thenReturn(List.of(
                        Role.builder()
                                .name("ADMIN")
                                .permissions(permissions)
                                .build()
                ));
        List<RoleResponseDTO> result = roleService.getAllRolesWithPermissions();
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("ADMIN", result.getFirst().getName());
        Assertions.assertEquals(1, result.getFirst().getPermissions().size());
    }
}

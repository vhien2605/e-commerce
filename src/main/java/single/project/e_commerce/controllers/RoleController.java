package single.project.e_commerce.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import single.project.e_commerce.dto.request.RoleRequestDTO;
import single.project.e_commerce.dto.response.ApiResponse;
import single.project.e_commerce.dto.response.ApiSuccessResponse;
import single.project.e_commerce.services.RoleService;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
@Slf4j
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/")
    @PreAuthorize("hasAuthority('create_role')")
    public ApiResponse createRole(@RequestBody @Valid RoleRequestDTO requestDTO) {
        return ApiSuccessResponse.builder()
                .data(roleService.createRole(requestDTO))
                .status(HttpStatus.OK.value())
                .message("create role successfully!")
                .build();
    }

    @PatchMapping("/update/{roleId}")
    @PreAuthorize("hasAuthority('update_role')")
    public ApiResponse createRole(@PathVariable Long roleId
            , @RequestBody @Valid RoleRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .data(roleService.updateRole(roleId, dto))
                .status(HttpStatus.OK.value())
                .message("update role successfully!")
                .build();
    }


    @GetMapping("/")
    @PreAuthorize("hasAuthority('read_role')")
    public ApiResponse readRole(@RequestBody String name) {
        return ApiSuccessResponse.builder()
                .data(roleService.getAllRolesWithPermissions())
                .status(HttpStatus.OK.value())
                .message("read all role successfully!")
                .build();
    }
}

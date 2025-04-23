package single.project.e_commerce.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import single.project.e_commerce.dto.request.UserRequestDTO;
import single.project.e_commerce.dto.request.UserUpdateRequestDTO;
import single.project.e_commerce.dto.response.ApiResponse;
import single.project.e_commerce.dto.response.ApiSuccessResponse;
import single.project.e_commerce.services.UserService;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping("/")
    public ApiResponse getAllUser() {
        return ApiSuccessResponse.builder()
                .data(userService.getAllUsers())
                .status(HttpStatus.OK.value())
                .message("Get all users successfully!")
                .build();
    }


    @GetMapping("/advanced-filter")
    public ApiResponse getAllUsersByAdvancedFilterAndPagination(
            Pageable pageable,
            @RequestParam(name = "user", required = false) String[] user,
            @RequestParam(name = "sortBy", defaultValue = "id:asc") String[] sortBy
    ) {
        return ApiSuccessResponse.builder()
                .data(userService.getAllUsersAdvancedFilterAndPagination(pageable, user, sortBy))
                .status(HttpStatus.OK.value())
                .message("Get all users successfully!")
                .build();
    }

    @GetMapping("/filter")
    public ApiResponse getAllUsersByAdvancedFilter(@RequestParam(name = "user", required = false) String[] user,
                                                   @RequestParam(name = "sortBy", defaultValue = "id:asc") String[] sortBy
    ) {
        return ApiSuccessResponse.builder()
                .data(userService.getAllUsersAdvancedFilter(user, sortBy))
                .status(HttpStatus.OK.value())
                .message("Get all users successfully!")
                .build();
    }


    @PostMapping("/")
    public ApiResponse createUser(@RequestBody @Valid UserRequestDTO request) {
        return ApiSuccessResponse.builder()
                .data(userService.saveUser(request))
                .status(HttpStatus.OK.value())
                .message("create user successfully")
                .build();
    }


    @PatchMapping("/update/{id}")
    public ApiResponse updateUser(@PathVariable @Min(value = 1, message = "userId must be greater than or equals 1") Long id,
                                  @RequestBody @Valid UserUpdateRequestDTO updateUserDTO) {
        log.info("start updateUser controller method");
        return ApiSuccessResponse.builder()
                .data(userService.updateUser(id, updateUserDTO))
                .status(HttpStatus.OK.value())
                .message("update user successfully")
                .build();
    }
}

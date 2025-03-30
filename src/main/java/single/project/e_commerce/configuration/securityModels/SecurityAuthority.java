package single.project.e_commerce.configuration.securityModels;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.models.Permission;
import single.project.e_commerce.models.Role;
import single.project.e_commerce.utils.enums.ErrorCode;

@RequiredArgsConstructor
@Slf4j
public class SecurityAuthority<T> implements GrantedAuthority {
    private final T authority;

    @Override
    public String getAuthority() {
        if (authority instanceof Role) {
            return "ROLE_" + ((Role) authority).getName();
        } else if (authority instanceof Permission) {
            return ((Permission) authority).getName();
        }
        throw new AppException(ErrorCode.AUTHORITY_TYPE_INVALID);
    }
}
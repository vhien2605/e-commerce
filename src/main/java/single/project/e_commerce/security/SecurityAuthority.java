package single.project.e_commerce.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import single.project.e_commerce.exceptions.DataInvalidException;
import single.project.e_commerce.models.Permission;
import single.project.e_commerce.models.Role;

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
        throw new DataInvalidException("Authority type is invalid!");
    }
}
package single.project.e_commerce.security.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import single.project.e_commerce.exceptions.DataInvalidException;
import single.project.e_commerce.models.User;
import single.project.e_commerce.repositories.UserRepository;
import single.project.e_commerce.security.auth_entities.SecurityUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("---------------------loadUserByUsername method start--------------------");
        User user = userRepository.findUserWithRoleAndPermissionByUsername(username)
                .orElseThrow(() -> new DataInvalidException("There isn't any user with the username " + username));
        return new SecurityUser(user);
    }
}

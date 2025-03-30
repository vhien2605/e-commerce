package single.project.e_commerce.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import single.project.e_commerce.models.RedisToken;
import single.project.e_commerce.repositories.RedisTokenRepository;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RedisTokenService {
    private final RedisTokenRepository tokenRepository;

    public RedisToken saveToken(RedisToken access) {
        return tokenRepository.save(access);
    }

    public Optional<RedisToken> findByJti(String jti) {
        return tokenRepository.findById(jti);
    }
}

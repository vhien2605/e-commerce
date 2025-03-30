package single.project.e_commerce.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import single.project.e_commerce.models.Token;
import single.project.e_commerce.repositories.TokenRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public Token saveToken(Token token) {
        return tokenRepository.save(token);
    }

    public void deleteById(String id) {
        tokenRepository.deleteById(id);
    }

    public Optional<Token> findById(String id) {
        return tokenRepository.findById(id);
    }
}

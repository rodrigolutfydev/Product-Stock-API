package br.com.lutfy.product_stock.infra.security;

import br.com.lutfy.product_stock.domain.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    public String tokenGenerate(User user) {
        try {
            var algotithm = Algorithm.HMAC256("12345678");
            return JWT.create()
                    .withIssuer("API Product Stock")
                    .withSubject(user.getLogin())
                    .withExpiresAt(expirationDate())
                    .sign(algotithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("error generate token JWT", exception);
        }
    }

    private Instant expirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}


package VictorCode.AuthModule.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;


// Essa classe gera e valida tokens JWT,
// além de extrair o nome do usuário do token.
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;  // chave secreta do token (configurada no application.properties)


    @Value("${jwt.expiration}")
    private long jwtExpirationMs; // tempo de expiração do token



    // Gera um token JWT baseado no nome de usuário
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    // Pega o nome do usuário a partir do token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    // Valida se o token é válido
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecret.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Token inválido: " + e.getMessage());
        }
        return false;
    }


}


/*
📘 Explicação rápida:

generateToken() → cria o token com data de expiração.

getUsernameFromToken() → lê o subject (nome do usuário).

validateToken() → verifica se o token é válido (não expirado, nem corrompido).

*/
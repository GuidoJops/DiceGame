package DiceGame.security.jwt;

import DiceGame.security.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static DiceGame.security.jwt.JwtConstants.JWT_EXPIRATION_MS;
import static DiceGame.security.jwt.JwtConstants.JWT_SECRET;

@Slf4j
@Component
public class JwtUtils {

    private final HttpServletResponse response;

    public JwtUtils(HttpServletResponse response) {
        this.response = response;
    }

    public String generateToken(Authentication auth) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        log.info(user.toString());

        Map<String, Object> header = buildTokenHeader();
        Map<String, Object> payload = buildTokenPayload(user);

        String token = buildAndSignToken(user.getUsername(), header, payload);
        addTokenToResponseHeader(token);

        return token;
    }

    public String getUsernameFromJWT(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AuthenticationCredentialsNotFoundException("Expired or incorrect token.");
        }
    }


    private void addTokenToResponseHeader(String token) {
        response.setHeader("Authorization", "Bearer " + token);
    }

    private Map<String, Object> buildTokenHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "Bearer Token");
        header.put("alg", "HS512");
        return header;
    }

    private Map<String, Object> buildTokenPayload(CustomUserDetails user) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", user.getId());
        payload.put("admin", user.isAdmin());
        return payload;
    }

    private String buildAndSignToken(String subject, Map<String, Object> header, Map<String, Object> payload) {
        return Jwts.builder()
                .setHeader(header)
                .setSubject(subject)
                .addClaims(payload)
                .setIssuer("Dice-App")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

}

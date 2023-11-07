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

@Slf4j
@Component
public class JwtUtils {

    //@Value("${app.jwt.secret}")
    private final String JWT_SECRET;
    private final long JWT_EXPIRATION_MS;
    private final HttpServletResponse response;

    public JwtUtils(HttpServletResponse response) {
        this.response = response;
        JWT_SECRET = "QRSasfasfjjtriTUVWertertXYdZgdfgdgdfg";
        JWT_EXPIRATION_MS = 600000;  // 300000 = 5 minutos

    }

    //Genera el Token
    public String generateToken(Authentication auth) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        log.info(user.toString());

        //Datos del HEADER
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "Bearer Token");
        header.put("alg", "HS512");

        //Datos para PAYLOAD
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", user.getId());
        extraClaims.put("admin", user.isAdmin());

        //Genera Token
        String token = Jwts.builder()
                .setHeader(header)
                .setSubject(user.getUsername())
                .addClaims(extraClaims)
                .setIssuer("Dice-App")
                .setIssuedAt( new Date())
                .setExpiration(new Date (System.currentTimeMillis()+ JWT_EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
        addTokenToResponseHeader(token);
        return token;
    }

    //Extrae usuario del token
    public String getUsernameFromJWT (String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

    }

    //Valida el Token
    public boolean validateToken (String token) {
        try{
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AuthenticationCredentialsNotFoundException("Token expirado o incorrecto");
        }
    }


    //Agrega token en el Header del HttpServletResponse
    private void addTokenToResponseHeader(String token) {
        response.setHeader("Authorization", "Bearer " + token);
        log.info("Se agrego Token en el Header (Authorization)");

    }

}
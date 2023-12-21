package DiceGame.security.jwt;

import DiceGame.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Slf4j
public class JwtSecurityFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwtToken = getJwtTokenFromRequest(request);
            if (isTokenValid(jwtToken)) {
                authenticateUser(request, jwtToken);
            }
        } catch (Exception e) {
            log.error("User cannot be authenticated. {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer")) {
            return header.substring(7);
        }
        return null;
    }

    private boolean isTokenValid(String jwtToken) {
        return jwtToken != null && jwtUtils.validateToken(jwtToken);
    }

    private UserDetails getUserDetails(String jwtToken) {
        String username = jwtUtils.getUsernameFromJWT(jwtToken);
        return customUserDetailsService.loadUserByUsername(username);
    }

    private void authenticateUser(HttpServletRequest request, String jwtToken) {
        UserDetails userDetails = getUserDetails(jwtToken);
        WebAuthenticationDetails webAuthenticationDetails = new WebAuthenticationDetailsSource().buildDetails(request);
        UsernamePasswordAuthenticationToken authenticationToken = buildAuthenticationToken(userDetails);
        authenticationToken.setDetails(webAuthenticationDetails);
        setAuthenticationInSecurityContext(authenticationToken);
    }

    private UsernamePasswordAuthenticationToken buildAuthenticationToken(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private void setAuthenticationInSecurityContext(UsernamePasswordAuthenticationToken authenticationToken) {
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

}

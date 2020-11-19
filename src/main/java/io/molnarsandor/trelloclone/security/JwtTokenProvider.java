package io.molnarsandor.trelloclone.security;

import io.jsonwebtoken.*;
import io.molnarsandor.trelloclone.user.model.UserEntity;
import io.molnarsandor.trelloclone.user.UserDetailsImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.molnarsandor.trelloclone.security.SecurityConstants.EXPIRATION_TIME;
import static io.molnarsandor.trelloclone.security.SecurityConstants.SECRET;

@Component
public class JwtTokenProvider {

    private final Log log = LogFactory.getLog(this.getClass());

    public String generateToken(Authentication authentication) {
        UserEntity userEntity = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        Date now = new Date(System.currentTimeMillis());

        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        String userId = Long.toString(userEntity.getId());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", (Long.toString(userEntity.getId())));
        claims.put("username", userEntity.getEmail());
        claims.put("fullName", userEntity.getFullName());


        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    // Validate Token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT Signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT Token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT Token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT Token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        }
        return false;
    }

    // Get userId from Token
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        String id = (String)claims.get("id");

        return Long.parseLong(id);
    }
}

package jp.co.axa.apidemo.configs;

import io.jsonwebtoken.*;
import jp.co.axa.apidemo.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.slf4j.*;
import java.util.Date;
/*
* Class for Generate the jwt token
* Clim the token
* Parse the token and validate the token */
@Component
public class JwtTokenConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenConfig.class);
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000;

    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    /* Method Generate the token by user information by Request form controller or service
    * Add the token behavior and Digest option for generating token string
    * Add the subject which will be the jwt token information and expire time duration   */
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(String.format("%s,%s", user.getId(), user.getEmail()))
                .setIssuer("AxaLife")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();

    }

    /* Validate the token by Secret key by parching after requesting by service and controller form client
    * Using the server secret key to parse and validate */
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            LOGGER.error("JWT expired", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Token is null, empty or only whitespace", ex.getMessage());
        } catch (MalformedJwtException ex) {
            LOGGER.error("JWT is invalid", ex);
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("JWT is not supported", ex);
        } catch (SignatureException ex) {
            LOGGER.error("Signature validation failed");
        }
        return false;
    }

    /* Parse the subject which is built with username and email for user
    *  using for authentication and authorization and clime for validate user */
    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }
    /* Parse the jwt with secret to make clime for jwt token with Secret key */
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}

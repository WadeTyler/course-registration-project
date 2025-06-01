package net.tylerwade.registrationsystem.auth.token;

import jakarta.servlet.http.Cookie;
import net.tylerwade.registrationsystem.config.AppProperties;
import net.tylerwade.registrationsystem.config.security.jwt.JwtProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenServiceImpl implements TokenService {
    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;
    private final AppProperties appProperties;

    public TokenServiceImpl(JwtEncoder jwtEncoder, JwtProperties jwtProperties, AppProperties appProperties) {
        this.jwtEncoder = jwtEncoder;
        this.jwtProperties = jwtProperties;
        this.appProperties = appProperties;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        // Separate ROLE_* authorities for "roles", others for "scope"
        var authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        var roles = authorities.stream()
                .filter(auth -> auth.startsWith("ROLE_"))
                .toList();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.issuer())
                .issuedAt(now)
                .expiresAt(now.plus(jwtProperties.expirationMs(), ChronoUnit.MILLIS))
                .subject(authentication.getName())
                .claim("roles", roles)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    @Override
    public Cookie generateAuthTokenCookie(Authentication authentication) {
        Cookie cookie = new Cookie("AuthToken", generateToken(authentication));
        cookie.setPath("/");
        cookie.setSecure(appProperties.isProduction());
        cookie.setMaxAge((int) (jwtProperties.expirationMs() / 1000));
        cookie.setHttpOnly(true);

        return cookie;
    }


    @Override
    public Cookie generateLogoutCookie() {
        Cookie cookie = new Cookie("AuthToken", "");
        cookie.setPath("/");
        cookie.setSecure(appProperties.isProduction());
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);

        return cookie;
    }
}

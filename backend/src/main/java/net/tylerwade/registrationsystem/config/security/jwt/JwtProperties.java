package net.tylerwade.registrationsystem.config.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String secret, RSAPrivateKey privateKey, RSAPublicKey publicKey, String issuer, Long expirationMs) {

    public SecretKeySpec getSecretKey() {
        return new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

}

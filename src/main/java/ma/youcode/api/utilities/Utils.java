package ma.youcode.api.utilities;

import org.apache.commons.codec.binary.Hex;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.ResponseCookie;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Utils {

    public static String hashFingerprint(String fingerprint) {
        if (fingerprint == null) return null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(fingerprint.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing fingerprint", e);
        }
    }

    public static String generateFingerprint() {
        byte[] randomFgp = new byte[50];
        new SecureRandom().nextBytes(randomFgp);
        return Base64.getEncoder().encodeToString(randomFgp);
    }

    public static ResponseCookie buildFingerprintCookie(String value) {
        return ResponseCookie.from("__Secure-Fgp", value)
                .path("/")
                .sameSite(Cookie.SameSite.STRICT.name())
                .secure(true)
                .httpOnly(true)
                .build();
    }

}

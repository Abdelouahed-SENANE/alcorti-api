package ma.youcode.api.cache;


import ma.youcode.api.events.OnUserLogoutSuccessEvent;
import ma.youcode.api.security.jwt.JwtTokenProvider;
import net.jodah.expiringmap.ExpiringMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class LoggedOutTokenCache {

    private static final Logger log = LogManager.getLogger(LoggedOutTokenCache.class);
    private final ExpiringMap<String, OnUserLogoutSuccessEvent> tokenEventMap;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public LoggedOutTokenCache(@Value("${app.cache.logoutToken.maxSize}") int maxSize, JwtTokenProvider jwtTokenProvider) {
        this.tokenEventMap = ExpiringMap.builder()
                .maxSize(maxSize)
                .variableExpiration()
                .build();
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void markTokenAsLoggedOut(OnUserLogoutSuccessEvent event) {
        String token = event.getToken();
        if (tokenEventMap.containsKey(token)) {
            log.info("Log out token for user [{}] is already present in the cache", event.getUserCin());
        }else {
            Date tokenExpiryDate = jwtTokenProvider.getExpirationFromToken(token);
            long ttl = getTTLForToken(tokenExpiryDate);
            tokenEventMap.put(token, event, ttl, TimeUnit.SECONDS);
        }
    }

    public OnUserLogoutSuccessEvent getLogoutEventFromToken(String token) {
        return tokenEventMap.get(token);
    }

    private long getTTLForToken(Date date) {
        long secondAtExpiry = date.toInstant().getEpochSecond();
        long secondAtLogout = Instant.now().getEpochSecond();
        return Math.max(0 , secondAtExpiry - secondAtLogout);
    }
}

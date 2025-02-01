package ma.youcode.api.events.listeners;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.cache.LoggedOutTokenCache;
import ma.youcode.api.events.OnUserLogoutSuccessEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OnUserLogoutSuccessEventListener implements ApplicationListener<OnUserLogoutSuccessEvent> {

    private static final Logger log = LogManager.getLogger(OnUserLogoutSuccessEventListener.class);
    private final LoggedOutTokenCache tokenCache;
    @Override
    public void onApplicationEvent(@NotNull OnUserLogoutSuccessEvent event) {
        log.info("Token {} has been logged out for user [{}]", event.getToken(), event.getUserCin());
        tokenCache.markTokenAsLoggedOut(event);
    }
}

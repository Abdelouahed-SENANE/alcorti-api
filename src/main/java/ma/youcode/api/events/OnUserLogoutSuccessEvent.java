package ma.youcode.api.events;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
public class OnUserLogoutSuccessEvent extends ApplicationEvent {

    private final String userCin;
    private final String token;
    private final Date eventTime;

    public OnUserLogoutSuccessEvent(String userCin , String token) {
        super(userCin);
        this.eventTime = Date.from(Instant.now());
        this.userCin = userCin;
        this.token = token;
    }

}

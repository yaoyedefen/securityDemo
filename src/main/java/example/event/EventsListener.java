package example.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.security.authorization.event.AuthorizationGrantedEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * @Author shuaishuai.zhang1
 * @Date 2023/5/26
 */
@Component
@Slf4j
public class EventsListener {

    /**
     * 认证成功
     */
    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success){
        Authentication authentication = success.getAuthentication();
        log.info("{} successful authentication", authentication.getPrincipal());
    }

    /**
     * 认证失败
     */
    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failure){
        Authentication authentication = failure.getAuthentication();
        log.info("{} failed authentication", authentication.getPrincipal());
    }

    /**
     * 授权失败
     */
    @EventListener
    public void onFailure(AuthorizationDeniedEvent<Authentication> failure) {
        Authentication authentication = failure.getAuthentication().get();
        log.info("{} failed authorization", authentication.getPrincipal());
    }

    /**
     * 授权成功
     */
    @EventListener
    public void onSuccess(AuthorizationGrantedEvent<Authentication> success) {
        Authentication authentication = success.getAuthentication().get();
        log.info("{} successful authorization", authentication.getPrincipal());
    }


}

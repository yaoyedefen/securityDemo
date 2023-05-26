package example.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
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
public class AuthenticationEvents {
    /**
     * 授权失败
     */
    @EventListener
    public void onFailure(AuthorizationDeniedEvent<Authentication> failure) {
        Authentication authentication = failure.getAuthentication().get();
        log.info("{} failure", authentication.getPrincipal());
    }

    /**
     * 授权成功
     */
    @EventListener
    public void onSuccess(AuthorizationGrantedEvent<Authentication> failure) {
        Authentication authentication = failure.getAuthentication().get();
        log.info("{} success", authentication.getPrincipal());
    }
}

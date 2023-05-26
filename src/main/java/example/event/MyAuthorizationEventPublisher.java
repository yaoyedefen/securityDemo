package example.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authorization.AuthorityAuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationEventPublisher;
import org.springframework.security.authorization.SpringAuthorizationEventPublisher;
import org.springframework.security.authorization.event.AuthorizationGrantedEvent;
import org.springframework.security.authorization.method.ExpressionAttributeAuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @Author shuaishuai.zhang1
 * @Date 2023/5/26
 */
@Component
public class MyAuthorizationEventPublisher implements AuthorizationEventPublisher {
    private final ApplicationEventPublisher publisher;
    private final AuthorizationEventPublisher delegate;

    public MyAuthorizationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
        this.delegate = new SpringAuthorizationEventPublisher(publisher);
    }

    @Override
    public <T> void publishAuthorizationEvent(Supplier<Authentication> authentication,
                                              T object, AuthorizationDecision decision) {
        if (decision == null) {
            return;
        }
        if (!decision.isGranted()) {
            this.delegate.publishAuthorizationEvent(authentication, object, decision);
            return;
        }
        if (shouldThisEventBePublished(decision)) {
            AuthorizationGrantedEvent<T> granted = new AuthorizationGrantedEvent<>(
                    authentication, object, decision);
            this.publisher.publishEvent(granted);
        }
    }

    /**
     * 授权通知的允许逻辑
     *
     * @param decision 授权结果
     * @return boolean
     */
    private boolean shouldThisEventBePublished(AuthorizationDecision decision) {
        if (!(decision instanceof AuthorityAuthorizationDecision)) {
            if (decision instanceof ExpressionAttributeAuthorizationDecision) {
                String value = String.valueOf(((ExpressionAttributeAuthorizationDecision) decision).getExpressionAttribute());
                String authorities = value.substring(value.indexOf("(") + 1, value.indexOf(")"));
                String[] split = authorities.split(",");
                List<String> strings = Arrays.stream(split).map(s -> s.trim().substring(1, s.length() - 1)).collect(Collectors.toList());
                for (String s : strings) {
                    if (isShould(s)) {
                        return true;
                    }
                }
            }
            return false;
        }
        Collection<GrantedAuthority> authorities = ((AuthorityAuthorizationDecision) decision).getAuthorities();
        for (GrantedAuthority authority : authorities) {
            return isShould(authority.getAuthority());
        }
        return false;
    }

    private boolean isShould(String authority) {
        switch (authority) {
            case "ROLE_ADMIN":
            case "ROLE_USER":
                return true;
            default:
                return false;
        }
    }
}
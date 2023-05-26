package example.entity;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @Author shuaishuai.zhang1
 * @Date 2023/5/16
 */
public class CustomUser extends org.springframework.security.core.userdetails.User {
    public CustomUser(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), authorities);
    }
}

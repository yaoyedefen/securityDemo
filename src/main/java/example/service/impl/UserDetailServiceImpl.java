package example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import example.constant.AuthExceptionConstant;
import example.entity.CustomUser;
import example.entity.Role;
import example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static example.constant.AuthExceptionConstant.NO_USER;

/**
 * @Author shuaishuai.zhang1
 * @Date 2023/5/16
 */
@Component("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;
    private final StringRedisTemplate redisTemplate;
    public static final String ROLES_KEY = "_roles";

    @Autowired
    public UserDetailServiceImpl(UserServiceImpl userService, RoleServiceImpl roleService, StringRedisTemplate redisTemplate) {
        this.userService = userService;
        this.roleService = roleService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getById(username);
        if (Objects.isNull(user)) {
            throw AuthExceptionConstant.buildException(NO_USER.msg, NO_USER.errorCode, null);
        }
        user.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode(user.getPassword()));
        List<String> roles = null;
        Long size = redisTemplate.opsForList().size(username + ROLES_KEY);
        if (Objects.nonNull(size) && size.intValue() > 0) {
            roles = redisTemplate.opsForList().range(username + ROLES_KEY, 0, size);
        } else {
            QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>().eq("username", username);
            List<Role> list = roleService.list(queryWrapper);
            if (Objects.nonNull(list)) {
                roles = list.stream().map(Role::getRoleName).collect(Collectors.toList());
                redisTemplate.opsForList().rightPushAll(username + ROLES_KEY, roles);
            }
        }
        return new CustomUser(user, Optional.ofNullable(roles).orElse(Collections.emptyList()).
                stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    }
}

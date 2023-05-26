package example.filter;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.entity.Result;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static example.constant.AuthExceptionConstant.LOGIN_EXPIRED;
import static example.constant.AuthExceptionConstant.TOKEN_ERROR;
import static example.service.impl.UserDetailServiceImpl.ROLES_KEY;

/**
 * @Author shuaishuai.zhang1
 * @Date 2023/5/16
 */
public class TokenFilter extends OncePerRequestFilter {
    private final StringRedisTemplate redisTemplate;

    public TokenFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public static final String TOKEN = "token";
    public static final String USER_NAME = "username";
    public static final String LOGIN_MAPPING = "/login";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (LOGIN_MAPPING.equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader(TOKEN);
        String username = request.getHeader(USER_NAME);
        if (Objects.nonNull(username)) {
            Object tk = redisTemplate.opsForHash().get(username, TOKEN);
            if (!StringUtils.equals(token, String.valueOf(tk))) {
                buildResponse(response, Result.build(TOKEN_ERROR.errorCode, TOKEN_ERROR.msg, null));
                return;
            }
            after(username);
        } else {
            buildResponse(response, Result.build(LOGIN_EXPIRED.errorCode, LOGIN_EXPIRED.msg, null));
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void after(String username) {
        SecurityContext context = SecurityContextHolder.getContext();
        Long size = redisTemplate.opsForList().size(username + ROLES_KEY);
        if (Objects.nonNull(size)) {
            List<String> roles;
            roles = redisTemplate.opsForList().range(username + ROLES_KEY, 0, size);
            List<SimpleGrantedAuthority> authorities = Optional.ofNullable(roles).
                    orElse(Collections.emptyList()).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            context.setAuthentication(new UsernamePasswordAuthenticationToken(username, null, authorities));
        }
    }

    private void buildResponse(HttpServletResponse response, Object data) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.getWriter().write(new ObjectMapper().writeValueAsString(data));
    }
}

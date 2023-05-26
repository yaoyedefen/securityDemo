package example.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.constant.AuthExceptionConstant;
import example.entity.Result;
import example.entity.User;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static example.filter.TokenFilter.TOKEN;

/**
 * @Author shuaishuai.zhang1
 * @Date 2023/5/16
 */
public class RedisTokenLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final StringRedisTemplate redisTemplate;

    public RedisTokenLoginFilter(AuthenticationManager authenticationManager, StringRedisTemplate redisTemplate) {
        super(authenticationManager);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (IOException e) {
            throw AuthExceptionConstant.buildException(e.getMessage(), null);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        buildResponse(response, Collections.singletonMap("token", token(authResult)));
        SecurityContextHolder.getContext().setAuthentication(authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        buildResponse(response, Result.error(failed.getMessage(), failed.getCause().getMessage()));
    }

    private String token(Authentication authentication) {
        String name = authentication.getName();
        String token = UUID.nameUUIDFromBytes((name + new Random().ints().toString()).getBytes(StandardCharsets.UTF_8)).toString();
        redisTemplate.delete(name);
        redisTemplate.opsForHash().put(name, TOKEN, token);
        redisTemplate.expire(name, 10, TimeUnit.MINUTES);
        return token;
    }

    private void buildResponse(HttpServletResponse response, Object data) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.getWriter().write(new ObjectMapper().writeValueAsString(data));
    }
}

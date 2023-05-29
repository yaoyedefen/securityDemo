package example.config;

import example.filter.MyAccessDeniedHandler;
import example.filter.RedisTokenLoginFilter;
import example.filter.TokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationEventPublisher;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * Security configuration for the main application.
 *
 * @author Josh Cummings
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
public class RestConfig {


    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ApplicationContext context) throws Exception {
        // @formatter:off
        http
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated()
                )
                //.csrf((csrf) -> csrf.ignoringAntMatchers("/token"))
                //.httpBasic(Customizer.withDefaults())
                //token 拦截解析
                .addFilterBefore(new TokenFilter(redisTemplate), UsernamePasswordAuthenticationFilter.class)
                //登录拦截
                .addFilter(new RedisTokenLoginFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), redisTemplate))
                .exceptionHandling().accessDeniedHandler(new MyAccessDeniedHandler()).and()
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // @formatter:on
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired(required = false)
    void setAuthorizationEventPublisher(AuthorizationEventPublisher eventPublisher, @Qualifier("jsr250AuthorizationMethodInterceptor") AuthorizationManagerBeforeMethodInterceptor managerBeforeMethodInterceptor) {
        managerBeforeMethodInterceptor.setAuthorizationEventPublisher(eventPublisher);
    }

}

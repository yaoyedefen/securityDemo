package example.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.entity.Result;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.intercept.RequestMatcherDelegatingAuthorizationManager;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static example.constant.AuthExceptionConstant.ACCESS_DENIED;

/**
 * @Author shuaishuai.zhang1
 * @Date 2023/5/24
 */
public class AccessFilter extends GenericFilterBean {
    private final RequestMatcherDelegatingAuthorizationManager authorizationManager;

    public AccessFilter(RequestMatcherDelegatingAuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String alreadyFilteredAttributeName = getAlreadyFilteredAttributeName();
        request.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);
        try {
            AuthorizationDecision decision = this.authorizationManager.check(this::getAuthentication, request);
            if (decision != null && !decision.isGranted()) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
                response.getWriter().write(new ObjectMapper().writeValueAsString(Result.build(ACCESS_DENIED.errorCode, ACCESS_DENIED.msg, null)));
            }
            filterChain.doFilter(request, response);
        }
        finally {
            request.removeAttribute(alreadyFilteredAttributeName);
        }
    }
    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AuthenticationCredentialsNotFoundException(
                    "An Authentication object was not found in the SecurityContext");
        }
        return authentication;
    }

    private String getAlreadyFilteredAttributeName() {
        String name = getFilterName();
        if (name == null) {
            name = getClass().getName();
        }
        return name + ".APPLIED";
    }
}

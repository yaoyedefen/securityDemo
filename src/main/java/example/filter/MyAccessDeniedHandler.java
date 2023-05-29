package example.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.entity.Result;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static example.constant.AuthExceptionConstant.ACCESS_DENIED;

/**
 * @Author shuaishuai.zhang1
 * @Date 2023/5/29
 */
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        if (response.isCommitted()) {
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.getWriter().write(new ObjectMapper().writeValueAsString(Result.build(ACCESS_DENIED.errorCode, ACCESS_DENIED.msg, null)));
    }
}

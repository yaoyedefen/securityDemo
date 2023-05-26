package example.exception;

import example.entity.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author shuaishuai.zhang1
 * @Date 2023/5/23
 */
@RestControllerAdvice
public class ExceptionAdvise {
    @ExceptionHandler({AuthException.class})
    public Object handler(AuthException exception){
        return Result.error(exception.getMessage(), exception.getDetailMessage());
    }
}

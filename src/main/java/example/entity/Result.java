package example.entity;

import static org.springframework.http.HttpStatus.*;

/**
 * @Author shuaishuai.zhang1
 * @Date 2023/5/23
 */
public class Result<T> {
    private int code;
    private T data;
    private Object detailData;

    public Result(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public Result(int code, T data, Object detailData) {
        this.code = code;
        this.data = data;
        this.detailData = detailData;
    }

    public static Result<String> success(){
        return new Result<>(OK.value(), OK.getReasonPhrase());
    }

    public static Result<Object> success(Object data){
        return new Result<>(OK.value(), data);
    }

    public static Result<Object> success(Object data, Object detailData){
        return new Result<>(OK.value(), data, detailData);
    }

    public static Result<String> error(){
        return new Result<>(INTERNAL_SERVER_ERROR.value(), OK.getReasonPhrase());
    }

    public static Result<Object> error(Object data){
        return new Result<>(INTERNAL_SERVER_ERROR.value(), data);
    }

    public static Result<Object> error(Object data, Object detailData){
        return new Result<>(INTERNAL_SERVER_ERROR.value(), data, detailData);
    }

    public static Result<Object> build(int code, Object data, Object detailData){
        return new Result<>(code, data, detailData);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

package example.constant;

import example.exception.AuthException;

/**
 * @Author shuaishuai.zhang1
 * @Date 2023/5/23
 */
public enum AuthExceptionConstant{
    /**
     * 认证错误
     */
    AUTH_ERROR(500, "认证错误"),
    LOGIN_EXPIRED(501, "登录过期"),
    TOKEN_ERROR(502, "非法TOKEN"),
    NO_USER(503, "无此用户"),
    INNER_ERROR_JSON(504, "内部错误: JSON数据处理异常"),
    ACCESS_DENIED(505, "权限不足");

    public int errorCode;
    public String msg;

    AuthExceptionConstant(int errorCode, String msg) {
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public static AuthException buildException(AuthExceptionConstant exceptionConstant){
        return new AuthException(exceptionConstant.msg, exceptionConstant.errorCode);
    }

    public static AuthException buildException(AuthExceptionConstant exceptionConstant, String detailMsg){
        return new AuthException(exceptionConstant.msg, exceptionConstant.errorCode, detailMsg);
    }

    public static AuthException buildException(String message, int errorCode, String detailMessage) {
        return new AuthException(message, errorCode, detailMessage);
    }

    public static AuthException buildException(String message, String detailMessage) {
        return new AuthException(message, detailMessage);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

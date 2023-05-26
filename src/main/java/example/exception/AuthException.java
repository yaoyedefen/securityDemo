package example.exception;

/**
 * @Author shuaishuai.zhang1
 * @Date 2023/5/23
 */
public class AuthException extends RuntimeException{
    private int errorCode;
    private String detailMessage;

    public AuthException(int errorCode) {
        this.errorCode = errorCode;
    }

    public AuthException(String message, int errorCode, String detailMessage) {
        super(message);
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
    }

    public AuthException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AuthException(String message, String detailMessage) {
        super(message);
        this.detailMessage = detailMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }
}

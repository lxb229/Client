package com.guse.platform.common.utils;

/**
 * 系统异常处理
 * @author Administrator
 * @version V1.0
 */
public class SystemException extends RuntimeException {
    private static final long serialVersionUID = 5362638549651827865L;
    private String            errorCode;

    public SystemException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public SystemException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    public SystemException(String message) {
        super(message);
    }
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}

package com.guse.platform.common.base;

import java.io.Serializable;

/**
 * 统一 service 接口返回结果
 * @author nbin
 * @date 2017年7月17日 下午3:21:15 
 * @version V1.0
 */
public class Result<T> implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
     * 成功标识
     */
    private static final int OK = 10000;

    /**
     * 错误码
     */
    private int              errorCode;

    /**
     * 错误描述信息. 简单描述服务具体错误的原因.
     */
    private String           errorMsg;

    /**
     * 数据
     */
    private T                data;

    public Result() {
        this(OK, "ok");
    }

    public Result(T data) {
        this();
        this.data = data;
    }

    public Result(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public boolean isOk() {
        return errorCode == OK;
    }

    public Result<T> setErrorCode(final int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public Result<T> setErrorMsg(final String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public T getData() {
        return data;
    }

    public void setData(final T data) {
        this.data = data;
    }
}

package com.guse.platform.common.base;

import java.io.Serializable;

/**
 * 接口统一返回对象
 * @author nbin
 * @date 2017年7月17日 上午9:29:15 
 * @version V1.0
 */
public class AjaxResponse implements Serializable {
	
    private static final long serialVersionUID = 1L;

    private String            code;
    private String            msg;
    private Object            data;

    public AjaxResponse() {
    	this.code = Constant.CODE_SUCCESS;
    	this.msg = Constant.MSG_SUCCESS;
    }

    public static AjaxResponse success() {
        return new AjaxResponse();
    }

    public AjaxResponse(String code, String msg, Object data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public AjaxResponse(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public AjaxResponse(Object data) {
    	super();
    	this.data = data;
    	this.code = Constant.CODE_SUCCESS;
        this.msg = Constant.MSG_SUCCESS;
    }

    public static AjaxResponse success(Object data) {
        return new AjaxResponse(Constant.CODE_SUCCESS, Constant.MSG_SUCCESS, data);
    }

    public static AjaxResponse success(Object data, String msg) {
        return new AjaxResponse(Constant.CODE_SUCCESS, msg, data);
    }

    public static AjaxResponse judge(Boolean data) {
        if (data != null && data) {
            return new AjaxResponse(Constant.CODE_SUCCESS, Constant.MSG_SUCCESS, data);
        }
        return new AjaxResponse(Constant.CODE_ERROR, Constant.MSG_ERROR);
    }

    public static AjaxResponse error(String msg) {
        return new AjaxResponse(Constant.CODE_ERROR, msg);
    }

    public void isError() {
        setCode(Constant.CODE_ERROR);
        setMsg(Constant.MSG_ERROR);
    }

    public void isSuccess() {
        setCode(Constant.CODE_SUCCESS);
        setMsg(Constant.MSG_SUCCESS);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        //   data = null;

    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}

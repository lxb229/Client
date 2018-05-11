package com.wangzhixuan.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * post参数 必填属性校验工具类 主要校验实体bean 在bean中使用
 * @author nbin
 * @date 2017年7月17日 下午5:46:41 
 * @version V1.0
 */
public class ValidataBean {

    private String  msg;

    private boolean flag;

    public ValidataBean() {
        flag = false;
    }

    public void isSuccess() {
        flag = true;
        msg = null;
    }

   
    public String getMsg() {
        return msg;
    }

    
    public void setMsg(String msg) {
        this.msg = msg;
    }

    
    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}

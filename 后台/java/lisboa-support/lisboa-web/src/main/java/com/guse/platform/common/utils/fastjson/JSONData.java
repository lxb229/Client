package com.guse.platform.common.utils.fastjson;

/**
 * 服务层数据处理结果的描述对象<br><br>
 * 调用一个业务层的方法，那么在处理过程中必定有多种状态，<br>
 * 而这些状态是调用者所需要的。<br>
 * 当然调用者有时也很需要处理完后返回的数据，<br>
 * 所以可以想到，处理的状态和返回的数据都是需要的。<br>
 * 因而将之存放在一个称之为JSONData对象中，<br>
 * 最后再将这个对象放回给调用者，那么调用者可以得到所有想要的数据。<br>
 * 继而供调用者灵活使用。<br>
 */
public class JSONData<T> {
    /** 返回的数据 */
    private T       data;
    /** true表示正常执行，false表示一切异常行为 */
    private boolean state;
    /** 处理中的文字描述 */
    private String  message = "";

    public JSONData(boolean state, String massage, T data) {
        super();
        this.data = data;
        this.state = state;
        this.message = massage;
    }

    public JSONData(boolean state, String message) {
        super();
        this.state = state;
        this.message = message;
    }

    public JSONData() {
        super();
    }

    /**
     * 如果方法有数据返回，那么通过此方法获得。
     */
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 设置处理过程中的状态。
     */
    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 设置处理过程的文字描述，以供调用者明晰。
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 如果需要将此对象格式化成json数据，<br>
     * 那么又需要有选择性的格式化放回数据（某个对象）中的某些数据，<br>
     * 这样就可以使用此方法完成。<br>
     * 
     * @param keeped			true：格式化提供的属性；false：除了提供的属性都格式化。
     * @param propertyNames		提供的属性列表
     * @return	已过滤属性的JSONData对象。<br>
     *  		此时再对此JSONData对象进行json格式化，<br>
     *  		即可得到过滤属性值的json数据。
     */
    public JSONData<Object> dataFilter(final boolean keeped, final String... propertyNames) {
        JSONData<Object> JSONData = new JSONData<Object>();
        JSONData.setData(JSONUtil.toJSON(this.data, keeped, propertyNames));
        JSONData.setMessage(this.message);
        JSONData.setState(this.state);
        return JSONData;
    }
    
    /**
     * 对象的全息描述（字符串形式）<br>
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "JSONData [data=" + data + ", state=" + state + ", massage=" + message + "]";
    }
}

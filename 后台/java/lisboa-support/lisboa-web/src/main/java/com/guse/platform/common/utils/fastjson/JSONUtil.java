package com.guse.platform.common.utils.fastjson;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;

/**
 * 定性：<br>
 * 此类为JSON（隶属于fastjson.jar）的扩展类。<br>
 * 也即是说，使用本类需要依赖fastjson.jar。<br>
 * 
 * <br>
 * 主要功能：<br>
 * 1. 可以任意指定对象的属性列表，然后只对这些属性进行格式化。<br>
 * 2. 可以构建一个多节点的json数据。<br>
 */
public class JSONUtil extends JSON {
    
    private JSONObject job;
    
    /**
     * 有选择性的对一个对象中的属性进行格式化。<br>
     * 
     * @param object			需要格式化的对象
     * @param keeped			true：按提供的属性列表格式化对象。false：除了提供的属性列表不格式化，其它的属性都需要格式化。
     * @param propertyNames		传入对象的属性列表
     * @return	返回一个已过滤字段的对象，可以调用其toString方法，得到过滤的json数据
     */
    public static Object toJSON(final Object object, final boolean keeped, final String... propertyNames) {
        PropertyFilter filter = new PropertyFilter() {
            public boolean apply(Object source, String propertyName, Object value) {
                for (String name : propertyNames) {
                    if (name.equalsIgnoreCase(propertyName)) {
                        return keeped;
                    }
                }
                return !keeped;
            }
        };
        SerializeWriter writer = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(writer);
        serializer.getPropertyFilters().add(filter);
        serializer.write(object);
        return JSONUtil.parse(writer.toString());
    }

    /**
     * 有选择性的对一个对象中的属性进行格式化。<br>
     * 
     * @param object			需要格式化的对象
     * @param keeped			true：按提供的属性列表格式化对象。false：除了提供的属性列表不格式化，其它的属性都需要格式化。
     * @param propertyNames		传入对象的属性列表
     * @return	返回格式化完毕的json数据，字符串形式。
     */
    public static String toJSONString(final Object object, final boolean keeped, final String... propertyNames) {
        return toJSON(object, keeped, propertyNames).toString();
    }

    public JSONUtil() {
        job = new JSONObject();
    }

    /**
     * 方法使用： <br>
     * 1. 需要创建JSONUtil对象。<br>
     * 2. 使用对象实例调用此方法。<br>
     * 3. 最后调用toString或toJSONString获得格式化的json数据。<br>
     * 
     * <br>
     * 主要功能：<br>
     * 1. 可以添加任意节点，节点名由key值提供。<br>
     * 2. 同时可以有选择性的过滤对象的某些属性。<br>
     * 
     * @param key			节点名
     * @param object		节点对应的数据
     * @param keeped		true：按提供的属性列表格式化对象。false：除了提供的属性列表不格式化，其它的属性都需要格式化。
     * @param propertyNames	数据对象的属性列表
     */
    public void put(String key, final Object object, final boolean keeped, final String... propertyNames) {
        this.job.put(key, toJSON(object, keeped, propertyNames));
    }

    /**
     * 方法使用： <br>
     * 1. 需要创建JSONUtil对象。<br>
     * 2. 使用对象实例调用此方法。<br>
     * 3. 最后调用toString或toJSONString获得格式化的json数据。<br>
     * 
     * <br>
     * 主要功能：<br>
     * 1. 可以添加任意节点，节点名由key值提供。<br>
     */
    public void put(String key, final Object object) {
        this.job.put(key, toJSON(object));
    }

    /**
     * 
     * 说明：<br>
     * 当在使用类实例创建多节点json数据时，<br>
     * 最终获得json数据需要调用此方法或者toString方法。<br>
     * @see com.alibaba.fastjson.JSON#toJSONString()
     */
    @Override
    public String toJSONString() {
        return toJSONString(this.job);
    }
}

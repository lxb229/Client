package com.guse.platform.common.utils.serializer;

public interface Serialize {

    /**
     * 序列化
     * @param obj
     * @return
     */
    public <O, I> O serialize(I obj);

    /**
     * 反序列化
     * @param bytes
     * @return
     */
    public <O, I> O unSerialize(I bytes);

}

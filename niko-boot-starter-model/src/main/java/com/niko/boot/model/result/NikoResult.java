package com.niko.boot.model.result;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 统一响应对象
 * 参考SaResult实现
 */
public class NikoResult extends LinkedHashMap<String, Object> implements Serializable {
    
    // 序列化版本号
    private static final long serialVersionUID = 1L;
    
    // 预定的状态码
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_ERROR = 500;
    
    /**
     * 构建
     */
    public NikoResult() {
    }
    
    /**
     * 构建
     * @param code 状态码
     * @param msg 信息
     * @param data 数据
     */
    public NikoResult(int code, String msg, Object data) {
        this.setCode(code);
        this.setMsg(msg);
        this.setData(data);
    }
    
    /**
     * 根据 Map 快速构建
     * @param map /
     */
    public NikoResult(Map<String, ?> map) {
        this.setMap(map);
    }
    
    /**
     * 获取code
     * @return code
     */
    public Integer getCode() {
        return (Integer) this.get("code");
    }
    
    /**
     * 获取msg
     * @return msg
     */
    public String getMsg() {
        return (String) this.get("msg");
    }
    
    /**
     * 获取data
     * @return data
     */
    public Object getData() {
        return this.get("data");
    }
    
    /**
     * 给code赋值，连缀风格
     * @param code code
     * @return 对象自身
     */
    public NikoResult setCode(int code) {
        this.put("code", code);
        return this;
    }
    
    /**
     * 给msg赋值，连缀风格
     * @param msg msg
     * @return 对象自身
     */
    public NikoResult setMsg(String msg) {
        this.put("msg", msg);
        return this;
    }
    
    /**
     * 给data赋值，连缀风格
     * @param data data
     * @return 对象自身
     */
    public NikoResult setData(Object data) {
        this.put("data", data);
        return this;
    }
    
    /**
     * 写入一个值 自定义key, 连缀风格
     * @param key key
     * @param data data
     * @return 对象自身
     */
    public NikoResult set(String key, Object data) {
        this.put(key, data);
        return this;
    }
    
    /**
     * 获取一个值 根据自定义key
     * @param <T> 要转换为的类型
     * @param key key
     * @param cs 要转换为的类型
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> cs) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        if (cs.isInstance(value)) {
            return (T) value;
        }
        // 简单类型转换
        if (cs == String.class) {
            return (T) String.valueOf(value);
        }
        if (cs == Integer.class && value instanceof Number) {
            return (T) Integer.valueOf(((Number) value).intValue());
        }
        if (cs == Long.class && value instanceof Number) {
            return (T) Long.valueOf(((Number) value).longValue());
        }
        if (cs == Double.class && value instanceof Number) {
            return (T) Double.valueOf(((Number) value).doubleValue());
        }
        if (cs == Boolean.class && value instanceof Boolean) {
            return (T) value;
        }
        return null;
    }
    
    /**
     * 写入一个Map, 连缀风格
     * @param map map
     * @return 对象自身
     */
    public NikoResult setMap(Map<String, ?> map) {
        for (String key : map.keySet()) {
            this.put(key, map.get(key));
        }
        return this;
    }
    
    /**
     * 写入一个 json 字符串, 连缀风格
     * @param jsonString json 字符串
     * @return 对象自身
     */
    public NikoResult setJsonString(String jsonString) {
        // 简化实现：需要 JSON 库支持，这里先抛出异常提示
        throw new UnsupportedOperationException("setJsonString requires JSON library support");
    }
    
    /**
     * 移除默认属性（code、msg、data）, 连缀风格
     * @return 对象自身
     */
    public NikoResult removeDefaultFields() {
        this.remove("code");
        this.remove("msg");
        this.remove("data");
        return this;
    }
    
    /**
     * 移除非默认属性（code、msg、data）, 连缀风格
     * @return 对象自身
     */
    public NikoResult removeNonDefaultFields() {
        for (String key : this.keySet()) {
            if ("code".equals(key) || "msg".equals(key) || "data".equals(key)) {
                continue;
            }
            this.remove(key);
        }
        return this;
    }
    
    // ============================  静态方法快速构建  ==================================
    
    // 构建成功
    public static NikoResult ok() {
        return new NikoResult(CODE_SUCCESS, "ok", null);
    }
    
    public static NikoResult ok(String msg) {
        return new NikoResult(CODE_SUCCESS, msg, null);
    }
    
    public static NikoResult code(int code) {
        return new NikoResult(code, null, null);
    }
    
    public static NikoResult data(Object data) {
        return new NikoResult(CODE_SUCCESS, "ok", data);
    }
    
    // 构建失败
    public static NikoResult error() {
        return new NikoResult(CODE_ERROR, "error", null);
    }
    
    public static NikoResult error(String msg) {
        return new NikoResult(CODE_ERROR, msg, null);
    }
    
    // 构建指定状态码
    public static NikoResult get(int code, String msg, Object data) {
        return new NikoResult(code, msg, data);
    }
    
    // 构建一个空的
    public static NikoResult empty() {
        return new NikoResult();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{"
                + "\"code\": " + this.getCode()
                + ", \"msg\": " + transValue(this.getMsg())
                + ", \"data\": " + transValue(this.getData())
                + "}";
    }
    
    /**
     * 转换 value 值：
     * 	如果 value 值属于 String 类型，则在前后补上引号
     * 	如果 value 值属于其它类型，则原样返回
     *
     * @param value 具体要操作的值
     * @return 转换后的值
     */
    private String transValue(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return "\"" + value + "\"";
        }
        return String.valueOf(value);
    }
}

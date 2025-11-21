package com.niko.boot.model.result;

import java.io.Serializable;

/**
 * 统一响应对象
 * 参考SaResult（ChokResponse）实现，提供统一的响应格式
 */
public class NikoResult<T> implements NikoResultInterface<T>, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private boolean success = true;
    private String code = NikoResultConstants.SUCCESS_CODE;
    private String msg = "";
    private String path = "";
    private String timestamp = "";
    private T data;
    
    public NikoResult() {
        super();
    }
    
    public NikoResult(T data) {
        super();
        this.data = data;
    }
    
    /**
     * 成功响应
     */
    public static <T> NikoResult<T> success() {
        return success(null);
    }
    
    public static <T> NikoResult<T> success(T data) {
        NikoResult<T> result = new NikoResult<>(data);
        result.setSuccess(true);
        result.setCode(NikoResultConstants.SUCCESS_CODE);
        result.setMsg("操作成功");
        result.setTimestamp(String.valueOf(System.currentTimeMillis()));
        return result;
    }
    
    /**
     * 失败响应
     */
    public static <T> NikoResult<T> fail(String message) {
        return fail(NikoResultConstants.ERROR_CODE, message);
    }
    
    public static <T> NikoResult<T> fail(String code, String message) {
        NikoResult<T> result = new NikoResult<>();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(message);
        result.setTimestamp(String.valueOf(System.currentTimeMillis()));
        return result;
    }
    
    // Getters and Setters
    @Override
    public boolean isSuccess() {
        return success;
    }
    
    @Override
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    @Override
    public String getCode() {
        return code;
    }
    
    @Override
    public void setCode(String code) {
        this.code = code;
    }
    
    @Override
    public String getMsg() {
        return msg;
    }
    
    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    @Override
    public String getPath() {
        return path;
    }
    
    @Override
    public void setPath(String path) {
        this.path = path;
    }
    
    @Override
    public String getTimestamp() {
        return timestamp;
    }
    
    @Override
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public T getData() {
        return data;
    }
    
    @Override
    public void setData(T data) {
        this.data = data;
    }
    
    @Override
    public String toString() {
        return "NikoResult{" +
                "success=" + success +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", path='" + path + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", data=" + data +
                '}';
    }
}


package com.niko.boot.model.result;

import java.io.Serializable;

/**
 * 响应对象接口
 * 参考SaResult实现
 */
public interface NikoResultInterface<T> extends Serializable {
    
    boolean isSuccess();
    void setSuccess(boolean success);
    
    String getCode();
    void setCode(String code);
    
    String getMsg();
    void setMsg(String msg);
    
    String getPath();
    void setPath(String path);
    
    String getTimestamp();
    void setTimestamp(String timestamp);
    
    T getData();
    void setData(T data);
}


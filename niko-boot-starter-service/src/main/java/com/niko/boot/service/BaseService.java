package com.niko.boot.service;

/**
 * Service 基类
 * 提供 Service 层的基础抽象，用户可继承此类实现业务逻辑
 * 
 * @author niko-boot
 * @since 1.0.0-SNAPSHOT
 */
public class BaseService {
    
    /**
     * 默认构造函数
     */
    public BaseService() {
        super();
    }
    
    // 预留扩展点，用户可在此基类上扩展通用业务方法
}


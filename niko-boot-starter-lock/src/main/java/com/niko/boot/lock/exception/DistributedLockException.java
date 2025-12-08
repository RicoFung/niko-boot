package com.niko.boot.lock.exception;

/**
 * 分布式锁异常
 * 用于标识分布式锁相关的异常，可以携带自定义的错误消息
 * 
 * @author EPO
 * @since 2025-01-15
 */
public class DistributedLockException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 构造分布式锁异常
     * 
     * @param message 错误消息（通常是 lockFailMsg）
     */
    public DistributedLockException(String message) {
        super(message);
    }
    
    /**
     * 构造分布式锁异常
     * 
     * @param message 错误消息
     * @param cause 原因异常
     */
    public DistributedLockException(String message, Throwable cause) {
        super(message, cause);
    }
}


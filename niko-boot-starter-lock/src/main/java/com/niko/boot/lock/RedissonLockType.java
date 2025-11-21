package com.niko.boot.lock;

/**
 * Redisson锁类型枚举
 * 封装chok2-lock的RedissonLockType
 */
public enum RedissonLockType {
    
    /** 可重入锁*/
    REENTRANT_LOCK,
    
    /** 公平锁*/
    FAIR_LOCK,
    
    /** 读锁*/
    READ_LOCK,
    
    /** 写锁*/
    WRITE_LOCK;
}


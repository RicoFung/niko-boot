package com.niko.boot.lock.aspect;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.niko.boot.lock.annotation.RedissonLock;
import com.niko.boot.lock.exception.DistributedLockException;
import com.niko.boot.lock.util.LockSpelExpressionParser;

/**
 * Redisson分布式锁切面
 * 封装chok2-lock的RedissonLockAspect
 */
@Component
@Scope
@Aspect
@Order(1)
public class RedissonLockAspect {
    
    @FunctionalInterface
    private interface TryLockFunction {
        boolean tryLock() throws InterruptedException;
    }
    
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private RedissonClient redissonClient;
    
    @Pointcut("@annotation(com.niko.boot.lock.annotation.RedissonLock)")
    private void lockPoint() {
    }
    
    @Around("lockPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);
        Object[] args = pjp.getArgs();
        
        // 解析锁key
        String lockKey = LockSpelExpressionParser.parseExpression(redissonLock.lockKey(), method, args);
        if (lockKey == null || lockKey.isEmpty()) {
            String failMsg = LockSpelExpressionParser.parseExpression(redissonLock.lockFailMsg(), method, args);
            log.error("锁key解析为空，表达式: [{}], 错误消息: {}", redissonLock.lockKey(), failMsg);
            throw new DistributedLockException(failMsg);
        }
        
        // 解析错误消息
        String failMsg = LockSpelExpressionParser.parseExpression(redissonLock.lockFailMsg(), method, args);
        
        // 获取锁对象并执行加锁逻辑
        RLock lock = getLock(lockKey, redissonLock);
        int tryLockOption = redissonLock.tryLockOption();
        
        switch (tryLockOption) {
            case 1: 
                return tryLock(lockKey, failMsg, lock, pjp, lock::tryLock);
            case 2: 
                return tryLock(lockKey, failMsg, lock, pjp, () -> lock.tryLock(redissonLock.waitTime(), redissonLock.timeUnit()));
            case 3: 
                return tryLock(lockKey, failMsg, lock, pjp, () -> lock.tryLock(redissonLock.waitTime(), redissonLock.leaseTime(), redissonLock.timeUnit()));
            default:
                throw new RuntimeException("不支持的tryLock选项: " + tryLockOption);
        }
    }
    
    /**
     * 统一的加锁逻辑
     * 
     * @param key 锁的key
     * @param failMsg 获取锁失败时的错误消息
     * @param lock 锁对象
     * @param pjp 连接点
     * @param tryLockFunc 尝试获取锁的函数
     * @return 方法执行结果
     * @throws Throwable 异常
     */
    private Object tryLock(String key, String failMsg, RLock lock, ProceedingJoinPoint pjp, 
                          TryLockFunction tryLockFunc) throws Throwable {
        boolean acquired;
        try {
            acquired = tryLockFunc.tryLock();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("获取锁时被中断", e);
        }
        
        if (acquired) {
            try {
                return pjp.proceed();
            } catch (Exception e) {
                log.error("执行加锁方法时发生异常，key: [{}]", key, e);
                throw e;
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        } else {
            log.warn("获取锁失败，key: [{}], 错误信息: {}", key, failMsg);
            throw new DistributedLockException(failMsg);
        }
    }
    
    private RLock getLock(String key, RedissonLock redissonLock) {
        switch (redissonLock.lockType()) {
            case REENTRANT_LOCK:
                return redissonClient.getLock(key);
            case FAIR_LOCK:
                return redissonClient.getFairLock(key);
            case READ_LOCK:
                return redissonClient.getReadWriteLock(key).readLock();
            case WRITE_LOCK:
                return redissonClient.getReadWriteLock(key).writeLock();
            default:
                throw new RuntimeException("不支持的锁类型: " + redissonLock.lockType().name());
        }
    }
}


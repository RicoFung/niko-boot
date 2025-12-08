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
    
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private RedissonClient redissonClient;
    
    @Pointcut("@annotation(com.niko.boot.lock.annotation.RedissonLock)")
    private void lockPoint() {
    }
    
    @Around("lockPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // 获取被AOP方法
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        
        // 获取注解的锁对象
        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);
        
        // 获取方法参数
        Object[] args = pjp.getArgs();
        
        // 获取锁key，支持 SpEL 表达式解析方法参数
        String keyExpression = redissonLock.lockKey();
        log.info("解析锁key表达式: [{}], 方法参数: {}", keyExpression, java.util.Arrays.toString(args));
        String lockKey = LockSpelExpressionParser.parseExpression(keyExpression, method, args);
        log.info("解析后的锁key: [{}]", lockKey);
        
        // 验证锁key不能为空
        if (lockKey == null || lockKey.isEmpty()) {
            String failMsgExpression = redissonLock.lockFailMsg();
            String failMsg = LockSpelExpressionParser.parseExpression(failMsgExpression, method, args);
            log.error("❌ 锁key解析为空或空字符串！表达式: [{}], 解析结果: [{}], 方法参数: {}, 错误消息: {}", 
                keyExpression, lockKey, java.util.Arrays.toString(args), failMsg);
            throw new DistributedLockException(failMsg);
        }
        
        log.info("锁key验证通过，将使用此key获取锁: [{}]", lockKey);
        
        // 解析 lockFailMsg（支持 SpEL 表达式）
        String failMsgExpression = redissonLock.lockFailMsg();
        log.info("解析lockFailMsg表达式: [{}], 方法参数: {}", failMsgExpression, java.util.Arrays.toString(args));
        String parsedFailMsg = LockSpelExpressionParser.parseExpression(failMsgExpression, method, args);
        log.info("解析后的lockFailMsg: [{}]", parsedFailMsg);
        
        // 获取等待锁的时间
        long waitTime = redissonLock.waitTime();
        // 获取持有锁的时间
        long leaseTime = redissonLock.leaseTime();
        // 获取加锁时间单位
        TimeUnit timeUnit = redissonLock.timeUnit();
        // 获取锁对象
        RLock lock = getLock(lockKey, redissonLock);
        // 获取tryLock选项
        int tryLockOption = redissonLock.tryLockOption();
        
        switch (tryLockOption) {
            case 1: return tryLock1(lockKey, parsedFailMsg, lock, pjp);
            case 2: return tryLock2(lockKey, parsedFailMsg, waitTime, timeUnit, lock, pjp);
            case 3: return tryLock3(lockKey, parsedFailMsg, waitTime, leaseTime, timeUnit, lock, pjp);
        }
        return null;
    }
    
    private Object tryLock1(String key, String failMsg, RLock lock, ProceedingJoinPoint pjp) throws Throwable {
        log.info("尝试获取分布式锁，key: [{}], method: {}", key, pjp.getSignature().toShortString());
        
        boolean lockAcquired = lock.tryLock();
        log.info("锁获取结果，key: [{}], acquired: {}", key, lockAcquired);
        
        if (lockAcquired) {
            try {
                log.info("成功获取锁，开始执行方法，key: [{}]", key);
                Object result = pjp.proceed();
                log.info("方法执行完成，释放锁，key: [{}]", key);
                return result;
            } catch (Exception e) {
                log.error("执行加锁方法时发生异常，key: [{}]", key, e);
                throw e; // 重新抛出异常，确保异常能够传播
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                    log.info("已释放锁，key: [{}]", key);
                }
            }
        } else {
            log.warn("获取锁失败，key: [{}], 错误信息: {}", key, failMsg);
            throw new DistributedLockException(failMsg);
        }
    }
    
    private Object tryLock2(String key, String failMsg, long waitTime, TimeUnit timeUnit, RLock lock, ProceedingJoinPoint pjp) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("Use tryLockOption is 2: tryLock(waitTime, timeUnit)");
        }
        
        if (lock.tryLock(waitTime, timeUnit)) {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("get lock success [{}]", key);
                }
                return pjp.proceed();
            } catch (Exception e) {
                log.error("execute locked method occured an exception", e);
                throw e; // 重新抛出异常，确保异常能够传播
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                    if (log.isDebugEnabled()) {
                        log.debug("release lock [{}]", key);
                    }
                }
            }
        } else {
            log.warn("获取锁失败，key: [{}], 错误信息: {}", key, failMsg);
            throw new DistributedLockException(failMsg);
        }
    }
    
    private Object tryLock3(String key, String failMsg, long waitTime, long leaseTime, TimeUnit timeUnit, RLock lock, ProceedingJoinPoint pjp) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("Use tryLockOption is 3: tryLock(waitTime, leaseTime, timeUnit)");
        }
        
        if (lock.tryLock(waitTime, leaseTime, timeUnit)) {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("get lock success [{}]", key);
                }
                return pjp.proceed();
            } catch (Exception e) {
                log.error("execute locked method occured an exception", e);
                throw e; // 重新抛出异常，确保异常能够传播
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                    if (log.isDebugEnabled()) {
                        log.debug("release lock [{}]", key);
                    }
                } else {
                    throw new RuntimeException("Business timeout ! lock [" + key + "] has been released !");
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
                throw new RuntimeException("do not support lock type:" + redissonLock.lockType().name());
        }
    }
}


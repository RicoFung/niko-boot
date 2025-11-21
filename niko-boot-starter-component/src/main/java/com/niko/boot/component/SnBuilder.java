package com.niko.boot.component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

/**
 * 流水号生成器
 * 封装chok2-component的SnBuilder
 * 基于Redis的流水号生成组件
 */
@Component
public class SnBuilder {
    
    private final static Logger logger = LoggerFactory.getLogger(SnBuilder.class);
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 批量生成流水号
     * @param k 流水号前缀
     * @param d 流水号位数
     * @param q 生成数量
     * @return 流水号数组
     */
    public String[] buildBatch(String k, int d, int q) {
        List<String> snList = new ArrayList<String>();
        for (int i = 0; i < q; i++) {
            String sn = build(k, d);
            if (null != sn) {
                snList.add(sn);
            }
        }
        String[] snArray = snList.toArray(new String[snList.size()]);
        return snArray;
    }
    
    /**
     * 生成单个流水号（默认过期时间1天）
     * @param k 流水号前缀
     * @param d 流水号位数
     * @return 流水号
     */
    public String build(String k, int d) {
        return build(k, d, 1, TimeUnit.DAYS);
    }
    
    /**
     * 生成单个流水号（自定义过期时间）
     * @param k 流水号前缀
     * @param d 流水号位数
     * @param timeout 过期时间
     * @param timeUnit 时间单位
     * @return 流水号
     */
    public String build(String k, int d, long timeout, java.util.concurrent.TimeUnit timeUnit) {
        // 初始化业务流水号
        String bizSn = null;
        // 初始化流水号计数器
        RedisAtomicLong snCounter = new RedisAtomicLong(k, redisTemplate.getConnectionFactory());
        // 获取当前流水号
        Long nowSn = snCounter.longValue();
        // 初始化新流水号
        Long newSn = 0L;
        // 初始化流水号上限
        Double maxSn = Math.pow(10, d) - 1;
        if (logger.isDebugEnabled()) {
            logger.debug("key <== {}, nowSn <== {}, maxSn <== {}", k, nowSn, maxSn);
        }
        // 校验流水号上限，超出返回null，反之返回业务流水号
        if (nowSn < maxSn) {
            newSn = snCounter.incrementAndGet();
            if ((null == newSn || newSn.longValue() == 1)) {
                // 初始设置过期时间
                snCounter.expire(timeout, timeUnit);
            }
            // 格式化按位数补0
            String suffixNum = String.format("%0" + d + "d", newSn);
            bizSn = k + suffixNum;
        }
        return bizSn;
    }
}


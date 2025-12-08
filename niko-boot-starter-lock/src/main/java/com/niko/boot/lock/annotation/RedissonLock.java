package com.niko.boot.lock.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import com.niko.boot.lock.RedissonLockType;

/**
 * 分布式锁注解
 * 封装RedissonLock注解，支持 SpEL 表达式读取方法参数
 * 
 * <p><b>SpEL 表达式支持：</b></p>
 * <p>lockKey 支持 SpEL 表达式语法，可以从方法参数中动态读取值：</p>
 * <ul>
 *   <li><b>普通字符串：</b>{@code "GEN_SN2_LOCK"} - 直接使用固定字符串</li>
 *   <li><b>参数引用：</b>{@code "#{#key}"} - 读取方法参数 key 的值</li>
 *   <li><b>混合表达式：</b>{@code "order:#{#orderId}"} - 字符串拼接参数值</li>
 *   <li><b>嵌套属性：</b>{@code "#{#order.id}"} - 读取对象属性的值</li>
 *   <li><b>参数索引：</b>{@code "#{#p0}"} 或 {@code "#{#a0}"} - 使用参数索引访问</li>
 * </ul>
 * 
 * <p><b>使用示例：</b></p>
 * <pre>{@code
 * // 固定 key
 * @RedissonLock(lockKey = "GEN_SN2_LOCK")
 * public void process() { }
 * 
 * // 使用参数
 * @RedissonLock(lockKey = "order:#{#orderId}")
 * public void processOrder(Long orderId) { }
 * 
 * // 多个参数
 * @RedissonLock(lockKey = "user:#{#userId}:order:#{#orderId}")
 * public void processUserOrder(Long userId, Long orderId) { }
 * 
 * // 对象属性
 * @RedissonLock(lockKey = "order:#{#order.id}")
 * public void processOrder(Order order) { }
 * 
 * // 参数索引（当参数名不可用时）
 * @RedissonLock(lockKey = "order:#{#p0}")
 * public void processOrder(Long orderId) { }
 * }</pre>
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RedissonLock {
    
    /**
     * 分布式锁的key
     * <p>支持 SpEL 表达式语法，可以从方法参数中动态读取值。</p>
     * <p>示例：</p>
     * <ul>
     *   <li>{@code "GEN_SN2_LOCK"} - 固定字符串</li>
     *   <li>{@code "order:#{#orderId}"} - 读取参数 orderId 的值</li>
     *   <li>{@code "user:#{#user.id}:order:#{#orderId}"} - 多个参数和嵌套属性</li>
     * </ul>
     * 
     * @return 锁的 key，支持 SpEL 表达式
     */
    String lockKey() default "";
    
    /**
     * 锁类型
     * 
     * @return
     */
    RedissonLockType lockType() default RedissonLockType.REENTRANT_LOCK;
    
    /**
     * 加锁失败提示
     * <p>支持 SpEL 表达式语法，可以从方法参数中动态读取值，用法与 lockKey 相同。</p>
     * <p>示例：</p>
     * <ul>
     *   <li>{@code "分布式锁获取失败，请稍后重试！"} - 固定字符串</li>
     *   <li>{@code "订单 #{#orderId} 正在处理中，请稍后重试！"} - 读取参数 orderId 的值</li>
     *   <li>{@code "用户 #{#userId} 的订单 #{#orderId} 资源被占用！"} - 多个参数</li>
     * </ul>
     * 
     * @return 加锁失败时的提示信息，支持 SpEL 表达式
     */
    String lockFailMsg() default "get lock failed";
    
    /**
     * 选择调用哪个tryLock()方法。默认1。
     * 1:tryLock();
     * 2:tryLock(waitTime, timeUnit);
     * 3:tryLock(waitTime, leaseTime, timeUnit);
     * @return
     */
    int tryLockOption() default 1;
    
    /**
     * 尝试获取锁的最大等待时间，超过这个值，则认为获取锁失败。 
     * 默认一秒。 
     * 该字段只有当tryLock()返回true才有效。
     * 
     * @return
     */
    long waitTime() default 1 * 1000;
    
    /**
     * 锁的持有时间,超过这个时间锁会自动失效（值应设置为大于业务处理的时间，确保在锁有效期内业务能处理完）。 
     * 默认五秒。 
     * 超时时间过后，锁自动释放。 
     * 建议：尽量缩简需要加锁的业务逻辑。
     * 
     * @return
     */
    long leaseTime() default 5 * 1000;
    
    /**
     * 时间格式 默认：毫秒
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}


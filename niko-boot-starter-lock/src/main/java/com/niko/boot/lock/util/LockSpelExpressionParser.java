package com.niko.boot.lock.util;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * SpEL 表达式解析工具类
 * 用于解析注解中的 SpEL 表达式，支持读取方法参数
 * 
 * <p>支持的表达式格式：</p>
 * <ul>
 *   <li>普通字符串：{@code "GEN_SN2_LOCK"} - 直接返回</li>
 *   <li>完整 SpEL 表达式：{@code "#{#key}"} - 解析为参数值</li>
 *   <li>混合表达式：{@code "order:#{#orderId}"} - 解析后拼接</li>
 *   <li>嵌套属性：{@code "#{#order.id}"} - 支持对象属性访问</li>
 *   <li>参数索引：{@code "#{#p0}"} 或 {@code "#{#a0}"} - 使用参数索引</li>
 * </ul>
 * 
 * <p>使用示例：</p>
 * <pre>{@code
 * @RedissonLock(lockKey = "order:#{#orderId}")
 * public void processOrder(Long orderId) { }
 * 
 * @RedissonLock(lockKey = "user:#{#user.id}:order:#{#orderId}")
 * public void processUserOrder(User user, Long orderId) { }
 * }</pre>
 * 
 * @author EPO
 * @since 2025-12-03
 */
public class LockSpelExpressionParser {
    
    private static final ExpressionParser parser = new SpelExpressionParser();
    private static final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    
    /**
     * 解析 SpEL 表达式，支持读取方法参数
     * 
     * @param expression SpEL 表达式字符串
     * @param method 被拦截的方法
     * @param args 方法参数值
     * @return 解析后的字符串
     */
    public static String parseExpression(String expression, Method method, Object[] args) {
        if (expression == null || expression.isEmpty()) {
            return expression;
        }
        
        // 如果表达式不包含 SpEL 语法（没有 #{}），直接返回
        if (!expression.contains("#{")) {
            return expression;
        }
        
        try {
            // 创建评估上下文
            EvaluationContext context = createEvaluationContext(method, args);
            
            // 如果整个表达式就是一个 SpEL 表达式（如 "#{#key}"），直接解析并返回
            if (expression.trim().startsWith("#{") && expression.trim().endsWith("}")) {
                String spelContent = expression.trim().substring(2, expression.trim().length() - 1);
                Expression expr = parser.parseExpression(spelContent);
                Object value = expr.getValue(context);
                // 如果值为 null，返回 null（而不是空字符串）
                return value != null ? value.toString() : null;
            }
            
            return parseSpelExpression(expression, context);
            
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger(LockSpelExpressionParser.class)
                .warn("SpEL表达式解析失败: {}, 错误: {}", expression, e.getMessage());
            return expression;
        }
    }
    
    /**
     * 解析字符串中的 SpEL 表达式片段
     * 支持混合表达式，如 "order:#{#orderId}" 
     */
    private static String parseSpelExpression(String expression, EvaluationContext context) {
        StringBuilder result = new StringBuilder();
        int start = 0;
        
        while (true) {
            int spelStart = expression.indexOf("#{", start);
            
            // 如果没有找到 SpEL 表达式，添加剩余文本并返回
            if (spelStart == -1) {
                result.append(expression.substring(start));
                break;
            }
            
            // 添加 SpEL 表达式之前的普通文本
            result.append(expression.substring(start, spelStart));
            
            // 查找对应的结束括号
            int depth = 0;
            int spelEnd = -1;
            
            for (int i = spelStart; i < expression.length(); i++) {
                char c = expression.charAt(i);
                if (c == '{') {
                    depth++;
                } else if (c == '}') {
                    depth--;
                    if (depth == 0) {
                        spelEnd = i;
                        break;
                    }
                }
            }
            
            // 如果没有找到匹配的结束括号，返回原字符串
            if (spelEnd == -1) {
                result.append(expression.substring(spelStart));
                break;
            }
            
            // 提取 SpEL 表达式内容（去掉 #{}）
            String spelContent = expression.substring(spelStart + 2, spelEnd);
            if (spelContent == null || spelContent.isEmpty()) {
                continue;
            }
            
            // 解析 SpEL 表达式
            Expression expr = parser.parseExpression(spelContent);
            Object value = expr.getValue(context);
            
            if (value != null) {
                result.append(value.toString());
            } else {
                result.append("null");
            }
            
            start = spelEnd + 1;
        }
        
        return result.toString();
    }
    
    /**
     * 创建评估上下文，将方法参数设置为变量
     * 支持多种参数访问方式：
     * 1. 参数名（如果可用）：{@code #key}, {@code #orderId}
     * 2. 参数索引：{@code #p0}, {@code #p1} 或 {@code #a0}, {@code #a1}
     * 
     * @param method 方法对象
     * @param args 方法参数值
     * @return 评估上下文
     */
    private static EvaluationContext createEvaluationContext(Method method, Object[] args) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        
        if (args == null || args.length == 0) {
            return context;
        }
        
        // 获取参数名称（Spring 可以通过字节码获取，即使编译时没有 -parameters）
        String[] parameterNames = null;
        if (method != null) {
            parameterNames = parameterNameDiscoverer.getParameterNames(method);
        }
        
        for (int i = 0; i < args.length; i++) {
            context.setVariable("p" + i, args[i]);
            context.setVariable("a" + i, args[i]);
            
            if (parameterNames != null && i < parameterNames.length) {
                String paramName = parameterNames[i];
                if (paramName != null && !paramName.matches("arg\\d+")) {
                    context.setVariable(paramName, args[i]);
                }
            }
        }
        
        return context;
    }
}


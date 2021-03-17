package org.jeecg.boot.starter.lock.aspect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zyf
 */
@Slf4j
public class BaseAspect {

    /**
     * 通过spring SpEL 获取参数
     *
     * @param key            定义的key值 以#开头 例如:#user
     * @param parameterNames 形参
     * @param values         形参值
     * @param keyConstant    key的常亮
     * @return
     */
    public List<String> getValueBySpEL(String key, String[] parameterNames, Object[] values, String keyConstant) {
        List<String> keys = new ArrayList<>();
        if (!key.contains("#")) {
            String s = "redis:lock:" + key + keyConstant;
            log.info("lockKey:" + s);
            keys.add(s);
            return keys;
        }
        //spel解析器
        ExpressionParser parser = new SpelExpressionParser();
        //spel上下文
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], values[i]);
        }
        Expression expression = parser.parseExpression(key);
        Object value = expression.getValue(context);
        if (value != null) {
            if (value instanceof List) {
                List value1 = (List) value;
                for (Object o : value1) {
                    addKeys(keys, o, keyConstant);
                }
            } else if (value.getClass().isArray()) {
                Object[] obj = (Object[]) value;
                for (Object o : obj) {
                    addKeys(keys, o, keyConstant);
                }
            } else {
                addKeys(keys, value, keyConstant);
            }
        }
        log.info("表达式key={},value={}", key, keys);
        return keys;
    }

    private void addKeys(List<String> keys, Object o, String keyConstant) {
        keys.add("redis:lock:" + o.toString() + keyConstant);
    }
}

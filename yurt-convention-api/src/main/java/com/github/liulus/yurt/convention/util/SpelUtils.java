package com.github.liulus.yurt.convention.util;

import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/9
 */
public abstract class SpelUtils {

    private static final SpelExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

    private static final MapAccessor MAP_ACCESSOR = new MapAccessor();

    private SpelUtils() {
    }

    public static Object getValue(String spel, Object root) {
        Asserts.notNull(root, "root object can not be null");
        StandardEvaluationContext context = new StandardEvaluationContext(root);
        if (root instanceof Map) {
            context.addPropertyAccessor(MAP_ACCESSOR);
        }
        return EXPRESSION_PARSER.parseExpression(spel).getValue(context);
    }

    public static <T> T getValue(String spel, Object root, Class<T> returnType) {
        Asserts.notNull(root, "root object can not be null");
        StandardEvaluationContext context = new StandardEvaluationContext(root);
        if (root instanceof Map) {
            context.addPropertyAccessor(MAP_ACCESSOR);
        }
        return EXPRESSION_PARSER.parseExpression(spel).getValue(context, returnType);
    }

}

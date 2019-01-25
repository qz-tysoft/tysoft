package com.tysoft.common;

import java.util.Collection;

import org.springframework.util.StringUtils;

import com.tysoft.common.Criterion.Operator;


public class Restrictions {
	/**
     * 等于
     */
    public static SimpleExpression eq(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && StringUtils.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Operator.EQ);
    }

    /**
     * 不等于
     */
    public static SimpleExpression ne(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && StringUtils.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Operator.NE);
    }

    /**
     * 模糊匹配
     */
    public static SimpleExpression like(String fieldName, String value, boolean ignoreNull) {
        if (ignoreNull && StringUtils.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Operator.LIKE);
    }
    
    /**
     * 模糊匹配，左相等
     */
    public static SimpleExpression leftLike(String fieldName, String value, boolean ignoreNull) {
        if (ignoreNull && StringUtils.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Operator.LEFTLIKE);
    }
    
    /**
     * 模糊匹配，右相等
     */
    public static SimpleExpression rightLike(String fieldName, String value, boolean ignoreNull) {
        if (ignoreNull && StringUtils.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Operator.RIGHTLIKE);
    }

    /**
     */
//    public static SimpleExpression like(String fieldName, String value,
//                                        MatchMode matchMode, boolean ignoreNull) {
//        if (StringUtils.isEmpty(value)) return null;
//        return null;
//    }

    /**
     * 大于
     */
    public static SimpleExpression gt(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && StringUtils.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Operator.GT);
    }

    /**
     * 小于
     */
    public static SimpleExpression lt(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && StringUtils.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Operator.LT);
    }

    /**
     * 小于等于
     */
    public static SimpleExpression lte(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && StringUtils.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Operator.LTE);
    }

    /**
     * 大于等于
     */
    public static SimpleExpression gte(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && StringUtils.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Operator.GTE);
    }
    
    /**
     * 是NULL
     */
    public static SimpleExpression isnull(String filedName){
    	return new SimpleExpression(filedName, null, Operator.ISNULL);
    }
    
    /**
     * 不是NULL
     */
    public static SimpleExpression isnotnull(String filedName){
    	return new SimpleExpression(filedName, null, Operator.ISNOTNULL);
    }

    /**
     * 并且
     */
    public static LogicalExpression and(Criterion... criterions) {
        return new LogicalExpression(criterions, Operator.AND);
    }

    /**
     * 或者
     */
    public static LogicalExpression or(Criterion... criterions) {
        return new LogicalExpression(criterions, Operator.OR);
    }

    /**
     * 包含于
     */
    @SuppressWarnings("rawtypes")
    public static LogicalExpression in(String fieldName, Collection value, boolean ignoreNull) {
        if (ignoreNull && (value == null || value.isEmpty())) {
            return null;
        }
        SimpleExpression[] ses = new SimpleExpression[value.size()];
        int i = 0;
        for (Object obj : value) {
            ses[i] = new SimpleExpression(fieldName, obj, Operator.EQ);
            i++;
        }
        return new LogicalExpression(ses, Operator.OR);
    }
    
    /**
     * 不包含
     */
    @SuppressWarnings("rawtypes")
    public static LogicalExpression notIn(String fieldName, Collection value, boolean ignoreNull) {
    	if (ignoreNull && (value == null || value.isEmpty())) {
            return null;
        }
    	SimpleExpression[] ses = new SimpleExpression[value.size()];
        int i = 0;
        for (Object obj : value) {
            ses[i] = new SimpleExpression(fieldName, obj, Operator.NE);
            i++;
        }
    	return new LogicalExpression(ses, Operator.AND);
    }
    
}

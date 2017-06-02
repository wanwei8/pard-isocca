package com.pard.common.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by wawe on 17/6/2.
 * AOP 工具类，用于进行常用的aop操作
 */
public class AopUtils {

    public static final String getMethodName(ProceedingJoinPoint pjp) {
        StringBuilder methodName = new StringBuilder(pjp.getSignature().getName()).append("(");
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String[] names = signature.getParameterNames();
        Class[] args = signature.getParameterTypes();
        for (int i = 0, len = args.length; i < len; i++) {
            if (i != 0) methodName.append(",");
            methodName.append(args[i].getSimpleName()).append(" ").append(names[i]);
        }
        return methodName.append(")").toString();
    }
}

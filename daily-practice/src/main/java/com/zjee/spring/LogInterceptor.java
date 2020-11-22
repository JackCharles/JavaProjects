package com.zjee.spring;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

public class LogInterceptor implements MethodInterceptor {


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {


        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("args", invocation.getArguments());
        Object result = invocation.proceed();
        context.setVariable("result", result);

        Method method = invocation.getMethod();
        Log annotation = method.getAnnotation(Log.class);
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(annotation.target());
        System.out.println(expression.getValue(context));

        return result;
    }
}

package site.higgs.limiter.expression;

import site.higgs.limiter.Limiter;

import java.lang.reflect.Method;

public class LimiterExpressionRootObject {

    private final Limiter limiter;

    private final Method targetMethod;

    private final Object[] args;

    private final Object target;

    private final Class<?> targetClass;


    public LimiterExpressionRootObject(
            Limiter limiter, Method targetMethod, Object[] args, Object target, Class<?> targetClass) {

        this.targetMethod = targetMethod;
        this.target = target;
        this.targetClass = targetClass;
        this.args = args;
        this.limiter = limiter;
    }


    public Limiter getLimiter() {
        return limiter;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getTarget() {
        return target;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }
}

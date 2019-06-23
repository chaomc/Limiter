package site.higgs.limiter.execute;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import site.higgs.limiter.ArgumentInjector;
import site.higgs.limiter.expression.LimiterOperationExpressionEvaluator;
import site.higgs.limiter.metadata.LimitedResourceMetadata;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 一个limiter执行的上下文
 */
public class LimiterExecutionContext {


    private static final HashMap<String, Object> emptyMap = new HashMap<>();

    private LimitedResourceMetadata metadata;

    private Object[] args;

    private Object target;

    private Map<String, Object> injectArgs;

    private LimiterOperationExpressionEvaluator evaluator;

    private BeanFactory beanFactory;

    private Object key;

    private Object fallbackResult;

    private Throwable throwable;

    public Throwable getThrowable() {
        return throwable;
    }

    public LimiterExecutionContext(LimitedResourceMetadata metadata, Object[] args, Object target, BeanFactory beanFactory) {
        this.metadata = metadata;
        this.args = extractArgs(metadata.getTargetMethod(), args);
        this.target = target;
        this.injectArgs = generateInjectArgs();
        this.beanFactory = beanFactory;
        this.evaluator = new LimiterOperationExpressionEvaluator();
        this.key = generateKey();
    }

    public boolean limit() {
        boolean ret;
        try {
            ret = this.metadata.getLimiter().limit(this.key, this.metadata.getLimiterParameters());
        } catch (Throwable throwable) {
            this.throwable = throwable;
            ret = this.metadata.getErrorHandler().resolve(throwable, this);
        }

        if (!ret) {
            this.fallbackResult = this.metadata.getFallback().resolve(this.metadata.getTargetMethod(), this.metadata.getTargetClass(), this.args, this.metadata.getLimitedResource(), this.target);
        }
        return ret;
    }

    public void release() {
        this.metadata.getLimiter().release(this.key, this.metadata.getLimiterParameters());
    }


    public Object getFallbackResult() {
        return fallbackResult;
    }

    private Object[] extractArgs(Method method, Object[] args) {
        if (!method.isVarArgs()) {
            return args;
        } else {
            Object[] varArgs = ObjectUtils.toObjectArray(args[args.length - 1]);
            Object[] combinedArgs = new Object[args.length - 1 + varArgs.length];
            System.arraycopy(args, 0, combinedArgs, 0, args.length - 1);
            System.arraycopy(varArgs, 0, combinedArgs, args.length - 1, varArgs.length);
            return combinedArgs;
        }
    }

    private Object generateKey() {
        if (StringUtils.hasText(this.metadata.getLimitedResource().getKey())) {
            EvaluationContext evaluationContext = evaluator.createEvaluationContext(this.metadata.getLimiter(), this.metadata.getTargetMethod(), this.args,
                    this.target, this.metadata.getTargetClass(), this.metadata.getTargetMethod(), injectArgs, beanFactory);
            Object evalKey = evaluator.key(this.metadata.getLimitedResource().getKey(), new AnnotatedElementKey(this.metadata.getTargetMethod(), this.metadata.getTargetClass()), evaluationContext);
            Assert.notNull(evalKey, "key值计算为null!");
            return evalKey;
        }
        return this.metadata.getTargetClass().getName() + "#" +
                this.metadata.getTargetMethod().getName();

    }

    private Map<String, Object> generateInjectArgs() {

        if (CollectionUtils.isEmpty(this.metadata.getArgumentInjectors())) {
            return emptyMap;
        }
        Map<String, Object> retVal = new HashMap<>();
        Collection<ArgumentInjector> argumentInjectors = this.metadata.getArgumentInjectors();
        for (ArgumentInjector argumentInjector : argumentInjectors) {
            Map<String, Object> tempMap = argumentInjector.inject(this.args);
            if (!tempMap.isEmpty()) {
                retVal.putAll(tempMap);
            }
        }
        return retVal;
    }


    public static HashMap<String, Object> getEmptyMap() {
        return emptyMap;
    }

    public LimitedResourceMetadata getMetadata() {
        return metadata;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getTarget() {
        return target;
    }

    public Map<String, Object> getInjectArgs() {
        return injectArgs;
    }

    public LimiterOperationExpressionEvaluator getEvaluator() {
        return evaluator;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public Object getKey() {
        return key;
    }
}

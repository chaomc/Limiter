package site.higgs.limiter.metadata;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import site.higgs.limiter.*;
import site.higgs.limiter.annotation.LimiterParameter;
import site.higgs.limiter.resource.LimitedResource;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public abstract class AbstractLimitedResourceMetadata<T extends LimitedResource> implements LimitedResourceMetadata<T>, Observer {

    private final BeanFactory beanFactory;

    private final T limitedResource;

    private Limiter limiter;

    private ErrorHandler errorHandler;

    private LimitedFallbackResolver limitedFallbackResolver;

    private Collection<ArgumentInjector> argumentInjectors;

    private Map<String, Object> limiterParameters;

    private Class<?> targetClass;

    private Method targetMethod;


    protected abstract void parseInternal(T limitedResource);

    public AbstractLimitedResourceMetadata(T limitedResource, Class<?> targetClass, Method targetMethod, BeanFactory beanFactory) {
        this.limitedResource = limitedResource;
        this.beanFactory = beanFactory;
        this.targetClass = targetClass;
        this.targetMethod = targetMethod;
        parse(limitedResource);
    }

    private void parse(T limitedResource) {
        this.limiter = (Limiter) this.beanFactory.getBean(limitedResource.getLimiter());
        this.errorHandler = (ErrorHandler) this.beanFactory.getBean(limitedResource.getErrorHandler());
        // 优先获取本类中的同名方法 而后从BeanFactory中获取
        try {
            final Method fallbackMethod = this.targetClass.getDeclaredMethod(limitedResource.getFallback(), this.targetMethod.getParameterTypes());
            fallbackMethod.setAccessible(true);
            this.limitedFallbackResolver = new LimitedFallbackResolver() {
                @Override
                public Object resolve(Method method, Class clazz, Object[] args, LimitedResource limitedResource, Object target) {
                    try {
                        return fallbackMethod.invoke(target, args);
                    } catch (IllegalAccessException e) {
                        return null;
                    } catch (InvocationTargetException e) {
                        return null;
                    }
                }
            };
        } catch (NoSuchMethodException e) {
            this.limitedFallbackResolver = (LimitedFallbackResolver) this.beanFactory.getBean(limitedResource.getFallback());
        }

        if (!CollectionUtils.isEmpty(limitedResource.getArgumentInjectors())) {
            argumentInjectors = new ArrayList<>();
            Collection<String> injectors = limitedResource.getArgumentInjectors();
            for (String si : injectors) {
                argumentInjectors.add((ArgumentInjector) this.beanFactory.getBean(si));
            }
        }
        this.limiterParameters = findLimiterParameters();
        this.parseInternal(limitedResource);
        if (limitedResource instanceof Observable) {
            ((Observable) limitedResource).addObserver(this);
        }

    }

    private Map<String, Object> findLimiterParameters() {
        // 获取所有LimiterParameter标记的字段
        Field[] fields = this.getLimitedResource().getClass().getDeclaredFields();
        Map<String, Object> retVal = null;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            if (AnnotatedElementUtils.hasAnnotation(field, LimiterParameter.class)) {
                if (retVal == null) {
                    retVal = new HashMap<>();
                }
                try {
                    retVal.put(field.getName(), field.get(this.getLimitedResource()));
                } catch (IllegalAccessException e) {
                }
            }
        }
        return retVal;
    }

    @Override
    public Map<String, Object> getLimiterParameters() {
        return limiterParameters;
    }

    @Override
    public T getLimitedResource() {
        return this.limitedResource;
    }

    @Override
    public void update(Observable o, Object arg) {
        parse(this.limitedResource);
    }


    @Override
    public Limiter getLimiter() {
        return limiter;
    }

    @Override
    public LimitedFallbackResolver getFallback() {
        return limitedFallbackResolver;
    }

    @Override
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    @Override
    public Collection<ArgumentInjector> getArgumentInjectors() {
        return argumentInjectors;
    }


    @Override
    public Class<?> getTargetClass() {
        return targetClass;
    }

    @Override
    public Method getTargetMethod() {
        return targetMethod;
    }
}

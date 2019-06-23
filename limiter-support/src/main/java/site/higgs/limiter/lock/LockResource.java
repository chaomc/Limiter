package site.higgs.limiter.lock;

import org.springframework.beans.factory.BeanFactory;
import site.higgs.limiter.metadata.LimitedResourceMetadata;
import site.higgs.limiter.resource.AbstractLimitedResource;

import java.lang.reflect.Method;
import java.util.Collection;

public class LockResource extends AbstractLimitedResource {
    public LockResource(String key, Collection<String> argumentInjectors, String fallback, String errorHandler, String limiter) {
        super(key, argumentInjectors, fallback, errorHandler, limiter);
    }

    @Override
    public LimitedResourceMetadata createMetadata(BeanFactory beanFactory, Class targetClass, Method targetMethod) {
        return new LockResourceMetadata(this, targetClass, targetMethod, beanFactory);

    }
}

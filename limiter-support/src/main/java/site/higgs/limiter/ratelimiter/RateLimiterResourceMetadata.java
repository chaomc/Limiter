package site.higgs.limiter.ratelimiter;

import org.springframework.beans.factory.BeanFactory;
import site.higgs.limiter.metadata.AbstractLimitedResourceMetadata;

import java.lang.reflect.Method;

public class RateLimiterResourceMetadata extends AbstractLimitedResourceMetadata<RateLimiterResource> {

    public RateLimiterResourceMetadata(RateLimiterResource limitedResource, Class<?> targetClass, Method targetMethod, BeanFactory beanFactory) {
        super(limitedResource, targetClass, targetMethod, beanFactory);
    }

    @Override
    protected void parseInternal(RateLimiterResource limitedResource) {

    }
}

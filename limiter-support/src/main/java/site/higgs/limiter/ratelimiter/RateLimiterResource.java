package site.higgs.limiter.ratelimiter;

import org.springframework.beans.factory.BeanFactory;
import site.higgs.limiter.annotation.LimiterParameter;
import site.higgs.limiter.metadata.LimitedResourceMetadata;
import site.higgs.limiter.resource.AbstractLimitedResource;

import java.lang.reflect.Method;
import java.util.Collection;

public class RateLimiterResource extends AbstractLimitedResource {

    @LimiterParameter
    private double rate;

    @LimiterParameter
    private long capacity;


    public RateLimiterResource(String key, Collection<String> argumentInjectors, String fallback, String errorHandler, String limiter, double rate,long capacity) {
        super(key, argumentInjectors, fallback, errorHandler, limiter);
        this.rate = rate;
        this.capacity = capacity;
    }

    @Override
    public LimitedResourceMetadata createMetadata(BeanFactory beanFactory, Class targetClass, Method targetMethod) {
        return new RateLimiterResourceMetadata(this, targetClass, targetMethod, beanFactory);

    }
}

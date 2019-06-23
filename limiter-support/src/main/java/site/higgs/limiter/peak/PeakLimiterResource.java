package site.higgs.limiter.peak;

import org.springframework.beans.factory.BeanFactory;
import site.higgs.limiter.annotation.LimiterParameter;
import site.higgs.limiter.metadata.LimitedResourceMetadata;
import site.higgs.limiter.resource.AbstractLimitedResource;

import java.lang.reflect.Method;
import java.util.Collection;

public class PeakLimiterResource extends AbstractLimitedResource {


    @LimiterParameter
    private int max;

    public PeakLimiterResource(String key, Collection<String> argumentInjectors, String fallback, String errorHandler, String limiter, int max) {
        super(key, argumentInjectors, fallback, errorHandler, limiter);
        this.max = max;
    }

    @Override
    public LimitedResourceMetadata createMetadata(BeanFactory beanFactory, Class targetClass, Method targetMethod) {
        return new PeakLimiterResourceMetadata(this, targetClass, targetMethod, beanFactory);

    }
}

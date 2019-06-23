package site.higgs.limiter.peak;

import org.springframework.beans.factory.BeanFactory;
import site.higgs.limiter.metadata.AbstractLimitedResourceMetadata;

import java.lang.reflect.Method;

public class PeakLimiterResourceMetadata extends AbstractLimitedResourceMetadata<PeakLimiterResource> {
    public PeakLimiterResourceMetadata(PeakLimiterResource limitedResource, Class<?> targetClass, Method targetMethod, BeanFactory beanFactory) {
        super(limitedResource, targetClass, targetMethod, beanFactory);
    }

    @Override
    protected void parseInternal(PeakLimiterResource limitedResource) {

    }
}

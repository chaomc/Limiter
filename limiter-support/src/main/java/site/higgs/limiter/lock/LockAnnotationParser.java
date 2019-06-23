package site.higgs.limiter.lock;

import org.springframework.core.annotation.AnnotationAttributes;
import site.higgs.limiter.AbstractLimiterAnnotationParser;
import site.higgs.limiter.resource.LimitedResource;

public class LockAnnotationParser extends AbstractLimiterAnnotationParser<Lock, HLock> {
    @Override
    public LimitedResource parseLimiterAnnotation(AnnotationAttributes attributes) {
        return new LockResource(
                getKey(attributes),
                getArgumentInjectors(attributes),
                getFallback(attributes),
                getErrorHandler(attributes),
                getLimiter(attributes)
        );
    }
}

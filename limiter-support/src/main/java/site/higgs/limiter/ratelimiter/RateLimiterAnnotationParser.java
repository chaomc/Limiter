package site.higgs.limiter.ratelimiter;

import org.springframework.core.annotation.AnnotationAttributes;
import site.higgs.limiter.AbstractLimiterAnnotationParser;
import site.higgs.limiter.resource.LimitedResource;

public class RateLimiterAnnotationParser extends AbstractLimiterAnnotationParser<RateLimiter, HRateLimiter> {
    @Override
    public LimitedResource<RateLimiter> parseLimiterAnnotation(AnnotationAttributes attributes) {
        return new RateLimiterResource(getKey(attributes),
                getArgumentInjectors(attributes),
                getFallback(attributes),
                getErrorHandler(attributes),
                getLimiter(attributes),
                (double) attributes.getNumber("rate"),
                (long) attributes.getNumber("capacity")
        );
    }
}

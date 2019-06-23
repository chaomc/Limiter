package site.higgs.limiter.peak;

import org.springframework.core.annotation.AnnotationAttributes;
import site.higgs.limiter.AbstractLimiterAnnotationParser;
import site.higgs.limiter.resource.LimitedResource;

public class PeakLimiterAnnotationParser extends AbstractLimiterAnnotationParser<PeakLimiter, HPeak> {
    @Override
    public LimitedResource<PeakLimiter> parseLimiterAnnotation(AnnotationAttributes attributes) {

        return new PeakLimiterResource(
                getKey(attributes),
                getArgumentInjectors(attributes),
                getFallback(attributes),
                getErrorHandler(attributes),
                getLimiter(attributes),
                (int) attributes.getNumber("max")
        );
    }
}

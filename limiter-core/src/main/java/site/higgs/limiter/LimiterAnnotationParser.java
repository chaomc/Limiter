package site.higgs.limiter;

import org.springframework.core.annotation.AnnotationAttributes;
import site.higgs.limiter.resource.LimitedResource;

import java.lang.annotation.Annotation;

public interface LimiterAnnotationParser<T extends Limiter> {


    Class<Annotation> getSupportAnnotation();

    LimitedResource<T> parseLimiterAnnotation(AnnotationAttributes attributes);
}

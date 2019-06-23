package site.higgs.limiter.metadata;

import site.higgs.limiter.*;
import site.higgs.limiter.resource.LimitedResource;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

public interface LimitedResourceMetadata<T extends LimitedResource> {


    Class<?> getTargetClass();

    Method getTargetMethod();

    T getLimitedResource();

    Limiter getLimiter();

    ErrorHandler getErrorHandler();

    LimitedFallbackResolver getFallback();

    Collection<ArgumentInjector> getArgumentInjectors();

    Map<String, Object> getLimiterParameters();

}

package site.higgs.limiter;

import site.higgs.limiter.resource.LimitedResource;

import java.lang.reflect.Method;

public interface LimitedFallbackResolver<T> {

    T resolve(Method method, Class<?> clazz, Object[] args, LimitedResource limitedResource, Object target);

}

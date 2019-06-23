package site.higgs.limiter.resource;

import org.springframework.beans.factory.BeanFactory;
import site.higgs.limiter.Limiter;
import site.higgs.limiter.metadata.LimitedResourceMetadata;

import java.lang.reflect.Method;
import java.util.Collection;

public interface LimitedResource<T extends Limiter> {


    String getKey();

    String getLimiter();

    String getFallback();

    String getErrorHandler();

    Collection<String> getArgumentInjectors();

    LimitedResourceMetadata createMetadata(BeanFactory beanFactory, Class<?> targetClass, Method targetMethod);

}

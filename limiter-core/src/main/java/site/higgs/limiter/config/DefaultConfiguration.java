package site.higgs.limiter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.higgs.limiter.ErrorHandler;
import site.higgs.limiter.LimitedFallbackResolver;
import site.higgs.limiter.execute.LimiterExecutionContext;
import site.higgs.limiter.resource.LimitedResource;

import java.lang.reflect.Method;

@Configuration
public class DefaultConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DefaultConfiguration.class);

    @Bean
    ErrorHandler defaultErrorHandler() {
        ErrorHandler errorHandler = new ErrorHandler() {
            @Override
            public boolean resolve(Throwable throwable, LimiterExecutionContext executionContext) {
                logger.info(throwable.getMessage());
                throw new RuntimeException(throwable.getMessage());
            }
        };
        return errorHandler;
    }

    @Bean
    LimitedFallbackResolver defaultFallbackResolver() {
        LimitedFallbackResolver limitedFallbackResolver
                = (method, clazz, args, limitedResource, target) -> {
            throw new RuntimeException("no message available");
        };
        return limitedFallbackResolver;
    }

}

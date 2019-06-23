package site.higgs.limiter.annotation;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import site.higgs.limiter.LimiterAnnotationParser;
import site.higgs.limiter.config.LimiterConfigurationSelector;

import java.lang.annotation.*;

import static org.springframework.context.annotation.AdviceMode.PROXY;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LimiterConfigurationSelector.class)
public @interface EnableLimiter {

    boolean proxyTargetClass() default false;

    int order() default Ordered.LOWEST_PRECEDENCE;

    /**
     * 默认有三种组件
     *
     * @return
     */
    String[] annotationParser()
            default {"site.higgs.limiter.lock.LockAnnotationParser",
            "site.higgs.limiter.ratelimiter.RateLimiterAnnotationParser",
            "site.higgs.limiter.peak.PeakLimiterAnnotationParser"
    };

    AdviceMode mode() default PROXY;

}

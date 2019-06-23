package site.higgs.limiter.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import site.higgs.limiter.source.DefaultLimitedResourceSource;
import site.higgs.limiter.source.LimitedResourceSource;
import site.higgs.limiter.LimiterAnnotationParser;
import site.higgs.limiter.interceptor.BeanFactoryLimitedResourceSourceAdvisor;
import site.higgs.limiter.interceptor.LimiterInterceptor;
import site.higgs.limiter.source.LimitedResourceScanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Import(DefaultConfiguration.class)
public class ProxyLimiterConfiguration extends AbstractLimiterConfiguration implements ResourceLoaderAware {

    ResourceLoader resourceLoader;

    @Bean(name = "site.higgs.limiter.config.internalLimiterAdvisor")
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BeanFactoryLimitedResourceSourceAdvisor limiterAdvisor() {
        BeanFactoryLimitedResourceSourceAdvisor advisor =
                new BeanFactoryLimitedResourceSourceAdvisor(limitedResourceSource());
        advisor.setAdvice(limiterInterceptor());
        if (this.enableLimiter != null) {
            advisor.setOrder(this.enableLimiter.<Integer>getNumber("order"));
        }
        return advisor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LimitedResourceSource limitedResourceSource() {
        String[] parsersClassNames = this.enableLimiter.getStringArray("annotationParser");
        List<String> defaultParsers = findDefaultParsers();
        if (!CollectionUtils.isEmpty(defaultParsers)) {
            int len = parsersClassNames.length;
            parsersClassNames = Arrays.copyOf(parsersClassNames, parsersClassNames.length + defaultParsers.size());
            for (int i = 0; i < defaultParsers.size(); i++) {
                parsersClassNames[i + len] = defaultParsers.get(i);
            }
        }
        LimiterAnnotationParser[] parsers = new LimiterAnnotationParser[parsersClassNames.length];
        for (int i = 0; i < parsersClassNames.length; i++) {
            try {
                Class<LimiterAnnotationParser> parserClass = (Class<LimiterAnnotationParser>) Class.forName(parsersClassNames[i]);
                parsers[i] = parserClass.newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("Class Not Found!");
            }
        }
        return new DefaultLimitedResourceSource(parsers);
    }


    private List<String> findDefaultParsers() {
        String[] parsers = new String[]{"site.higgs.limiter.lock.LockAnnotationParser",
                "site.higgs.limiter.ratelimiter.RateLimiterAnnotationParser",
                "site.higgs.limiter.peak.PeakLimiterAnnotationParser"
        };
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < parsers.length; i++) {
            try {
                Class.forName(parsers[i]);
                ret.add(parsers[i]);
            } catch (ClassNotFoundException e) {

            }
        }
        return ret;
    }


    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LimiterInterceptor limiterInterceptor() {
        LimiterInterceptor interceptor = new LimiterInterceptor();
        interceptor.setLimitedResourceSource(limitedResourceSource());
        return interceptor;
    }


    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}

package site.higgs.limiter.interceptor;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import site.higgs.limiter.source.LimitedResourceSource;


/**
 * 实际的切面
 */
public class BeanFactoryLimitedResourceSourceAdvisor extends AbstractBeanFactoryPointcutAdvisor {


    private LimitedResourceSource limitedResourceSource;

    /**
     * 切点
     */
    private final LimitedResourceSourcePointcut pointcut = new LimitedResourceSourcePointcut() {
        @Override
        protected LimitedResourceSource getLimitedResourceSource() {
            return BeanFactoryLimitedResourceSourceAdvisor.this.limitedResourceSource;
        }
    };

    public BeanFactoryLimitedResourceSourceAdvisor(LimitedResourceSource limitedResourceSource) {
        this.limitedResourceSource = limitedResourceSource;
    }


    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }
}

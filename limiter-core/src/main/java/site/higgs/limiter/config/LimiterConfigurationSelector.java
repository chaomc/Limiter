package site.higgs.limiter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AutoProxyRegistrar;
import org.springframework.util.StringUtils;
import site.higgs.limiter.annotation.EnableLimiter;

import java.util.ArrayList;
import java.util.List;


public class LimiterConfigurationSelector extends AdviceModeImportSelector<EnableLimiter> {

    private final Logger logger = LoggerFactory.getLogger(LimiterConfigurationSelector.class);

    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        logger.info("limiter start success...");
        switch (adviceMode) {
            case PROXY:
                return getProxyImports();
            case ASPECTJ:
                throw new RuntimeException("NotImplemented");
            default:
                return null;
        }
    }

    private String[] getProxyImports() {
        List<String> result = new ArrayList<>();
        result.add(AutoProxyRegistrar.class.getName());
        result.add(ProxyLimiterConfiguration.class.getName());
        return StringUtils.toStringArray(result);
    }


}

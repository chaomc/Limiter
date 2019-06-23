package site.higgs.limiter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import site.higgs.limiter.annotation.EnableLimiter;


@Configuration
public abstract class AbstractLimiterConfiguration implements ImportAware {


    protected AnnotationAttributes enableLimiter;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableLimiter = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableLimiter.class.getName(), false));
        if (this.enableLimiter == null) {
            throw new IllegalArgumentException(
                    "@EnableLimiter is not present on importing class " + importMetadata.getClassName());
        }
    }


}

package cc.sofast.infrastructure.func;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * @author wxl
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantFun {

    String tenant();

    String biz();

}
package cc.sofast.infrastructure.func;

import java.lang.annotation.*;

/**
 * @author wxl
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantBizFun {

    String tenant();

    String biz();

}
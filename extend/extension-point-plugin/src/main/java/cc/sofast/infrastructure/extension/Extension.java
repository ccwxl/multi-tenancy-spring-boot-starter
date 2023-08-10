package cc.sofast.infrastructure.extension;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Extension
 * @author fulan.zjf 2017-11-05
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(Extensions.class)
@Component
public @interface Extension {
    String tenant() default BizScenario.DEFAULT_TENANT;
    String bizId()  default BizScenario.DEFAULT_BIZ_ID;
    String scenario() default BizScenario.DEFAULT_SCENARIO;
}
package cn.lacia.wheel.spring.cq.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 你是电脑
 * @create 2019/11/27 - 18:39
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface LaciaSpringApplication {
    /**
     *
     * @return 包扫描路径
     */
    String value() default "";
}

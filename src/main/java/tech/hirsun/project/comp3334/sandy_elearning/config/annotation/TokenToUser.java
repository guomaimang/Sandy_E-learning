package tech.hirsun.project.comp3334.sandy_elearning.config.annotation;


import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenToUser {

    String value() default "user";

}

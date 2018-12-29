package cumt.innovative.training.project.basketballappointment.utils.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TableName {
    String value() default "";
}

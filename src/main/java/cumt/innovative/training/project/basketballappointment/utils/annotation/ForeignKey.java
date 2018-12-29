package cumt.innovative.training.project.basketballappointment.utils.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ForeignKey {
    Class<?> value();
    String splitString() default "null";
}

package at.malibu.api.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ValidateArgs {
    int minArgs() default 0;

    int maxArgs() default Integer.MAX_VALUE;
}

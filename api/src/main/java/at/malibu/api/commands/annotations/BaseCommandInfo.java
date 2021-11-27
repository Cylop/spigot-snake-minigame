package at.malibu.api.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BaseCommandInfo {
    String name();

    String[] labels() default {};

    String description() default "My command description";

    String usageMessage() default "Usage: /<command>";
}

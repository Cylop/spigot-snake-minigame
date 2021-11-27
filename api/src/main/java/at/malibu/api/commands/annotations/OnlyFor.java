package at.malibu.api.commands.annotations;

import at.malibu.api.commands.CommandExecutorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OnlyFor {
    CommandExecutorType[] executor();
}

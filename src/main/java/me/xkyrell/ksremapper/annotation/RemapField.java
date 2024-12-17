package me.xkyrell.ksremapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RemapField {

    Mode mode();

    boolean isStatic() default false;

    enum Mode {
        GET, SET
    }
}

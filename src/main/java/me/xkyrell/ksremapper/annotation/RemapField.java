package me.xkyrell.ksremapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.function.Function;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RemapField {

    Mode mode();

    boolean isStatic() default false;

    enum Mode implements Function<Method, Class<?>> {

        GET {
            @Override
            public Class<?> apply(Method method) {
                return method.getReturnType();
            }
        },
        SET {
            @Override
            public Class<?> apply(Method method) {
                return method.getParameterTypes()[0];
            }
        }
    }
}

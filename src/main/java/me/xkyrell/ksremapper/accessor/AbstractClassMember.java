package me.xkyrell.ksremapper.accessor;

import lombok.ToString;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;

@ToString(exclude = "remap")
public abstract class AbstractClassMember<T, A extends Annotation> {

    protected final A remap;

    protected MethodHandle methodHandle;

    public AbstractClassMember(Class<?> handleClass, String name, T type, A remap) {
        try {
            methodHandle = prepareAccessor(handleClass, name, type, remap);
        }
        catch (IllegalAccessException ex) {
            throw new RuntimeException(ex.getCause());
        }
        catch (ReflectiveOperationException ex) {
            throw new IllegalStateException(String.format(
                    "Reflection error occurred while preparing accessor for method %s in class %s",
                    name, handleClass), ex
            );
        }
        this.remap = remap;
    }

    protected abstract MethodHandle prepareAccessor(
            Class<?> handleClass, String name, T type, A remap
    ) throws ReflectiveOperationException;
}

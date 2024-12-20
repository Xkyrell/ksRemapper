package me.xkyrell.ksremapper.proxy;

import lombok.*;
import me.xkyrell.ksremapper.RemapConfig;
import me.xkyrell.ksremapper.RemapProxy;
import me.xkyrell.ksremapper.accessor.FieldAccessor;
import me.xkyrell.ksremapper.accessor.MethodAccessor;
import me.xkyrell.ksremapper.annotation.RemapField;
import me.xkyrell.ksremapper.annotation.RemapMethod;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class ProxyInvocationMapper {

    @Getter
    private final Class<? extends RemapProxy> proxyClass;
    private final RemapConfig config;

    public FieldAccessor findField(Method method, RemapField remap, Class<?> rawReturnType) {
        throw new UnsupportedOperationException("Method findField is not implemented");
    }

    public MethodHandle findConstructor(Class<?>[] rawTypes) {
        throw new UnsupportedOperationException("Method findConstructor is not implemented");
    }

    public MethodAccessor findMethod(Method method, RemapMethod remap, Class<?>[] rawTypes, Class<?> rawReturnType) {
        throw new UnsupportedOperationException("Method findMethod is not implemented");
    }

    public MethodHandle findObjectMethod(Method method) {
        throw new UnsupportedOperationException("Method findObjectMethod is not implemented");
    }

    public MethodHandle findDefaultMethod(Method method) {
        throw new UnsupportedOperationException("Method findDefaultMethod is not implemented");
    }

    public Class<?> getHandleClass() {
        return config.fetchHandle(proxyClass);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static abstract class AbstractMemberData<A extends Annotation> {

        protected final Method method;
        protected final Class<?> rawReturnType;
        protected final A remap;

    }

    @ToString
    @EqualsAndHashCode(callSuper = true)
    private static final class FieldData extends AbstractMemberData<RemapField> {

        private FieldData(Method method, Class<?> rawReturnType, RemapField remap) {
            super(method, rawReturnType, remap);
        }
    }

    @ToString
    @EqualsAndHashCode(callSuper = true)
    private static final class MethodData extends AbstractMemberData<RemapMethod> {

        private final Class<?>[] rawTypes;

        private MethodData(Method method, Class<?> rawReturnType, RemapMethod remap, Class<?>[] rawTypes) {
            super(method, rawReturnType, remap);

            this.rawTypes = rawTypes;
        }
    }
}

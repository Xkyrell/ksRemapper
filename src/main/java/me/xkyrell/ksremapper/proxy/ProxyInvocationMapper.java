package me.xkyrell.ksremapper.proxy;

import lombok.*;
import me.xkyrell.ksremapper.*;
import me.xkyrell.ksremapper.accessor.*;
import me.xkyrell.ksremapper.accessor.impl.*;
import me.xkyrell.ksremapper.annotation.RemapField;
import me.xkyrell.ksremapper.annotation.RemapMethod;
import me.xkyrell.ksremapper.util.TrustedLookup;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ProxyInvocationMapper {

    @Getter
    private final Class<? extends RemapProxy> proxyClass;
    private final RemapConfig config;

    private final Map<FieldData, FieldAccessor> setterFields = new HashMap<>();
    private final Map<FieldData, FieldAccessor> getterFields = new HashMap<>();
    private final Map<Class<?>[], MethodHandle> constructors = new HashMap<>();
    private final Map<MethodData, MethodAccessor> methods = new HashMap<>();
    private final Map<Method, MethodHandle> objectMethods = new HashMap<>();

    public FieldAccessor findField(Method method, RemapField remap, Class<?> rawReturnType) {
        FieldData data = new FieldData(method, rawReturnType, remap);
        return getFieldCache(remap.mode()).computeIfAbsent(data, __ -> {
            String fieldName = config.fetchMember(method);
            return new DynamicFieldAccessor(getHandleClass(), fieldName, remap.mode().apply(method), remap);
        });
    }

    private Map<FieldData, FieldAccessor> getFieldCache(RemapField.Mode mode) {
        return mode.equals(RemapField.Mode.SET)
                ? setterFields
                : getterFields;
    }

    public MethodHandle findConstructor(Class<?>[] rawTypes) {
        return constructors.computeIfAbsent(rawTypes, __ -> {
            try {
                MethodHandles.Lookup lookup = TrustedLookup.get();
                MethodType methodType = MethodType.methodType(void.class, rawTypes);
                return lookup.findConstructor(getHandleClass(), methodType);
            }
            catch (NoSuchMethodException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public MethodAccessor findMethod(Method method, RemapMethod remap, Class<?>[] rawTypes, Class<?> rawReturnType) {
        return methods.computeIfAbsent(new MethodData(method, rawReturnType, remap, rawTypes), __ -> {
            String methodName = config.fetchMember(method);
            MethodType methodType = MethodType.methodType(rawReturnType, rawTypes);
            return new DynamicMethodAccessor(getHandleClass(), methodName, methodType, remap);
        });
    }

    public MethodHandle findObjectMethod(Method method) {
        return objectMethods.computeIfAbsent(method, __ -> {
            MethodType methodType = MethodType.methodType(method.getReturnType(), method.getParameterTypes());
            try {
                return TrustedLookup.get().findVirtual(Object.class, method.getName(), methodType);
            }
            catch (NoSuchMethodException | IllegalAccessException ex) {
                throw new AssertionError(ex);
            }
        });
    }

    public MethodHandle findDefaultMethod(Method method) {
        return TrustedLookup.apply(lookup -> {
            try {
                return lookup.unreflectSpecial(method, method.getDeclaringClass());
            }
            catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        });
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

package me.xkyrell.ksremapper.proxy;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.reflect.Reflection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.xkyrell.ksremapper.*;
import me.xkyrell.ksremapper.util.TypeUtil;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationHandler;

@RequiredArgsConstructor
public class RemapProxyFactory {

    private final RemapConfig config;
    private final LoadingCache<Class<? extends RemapProxy>, ProxyInvocationMapper> mappers =
            CacheBuilder.newBuilder().build(CacheLoader.from(this::loadMapper));

    public RemapProxyFactory() {
        this(RemapConfig.builder().build());
    }

    public final <T extends RemapProxy> T remap(@NonNull Class<T> clazz, @NonNull Object handle) {
        ProxyInvocationMapper mapper = mappers.getUnchecked(clazz);

        Class<?> handleClass = mapper.getHandleClass();
        Class<?> actualClass = handle.getClass();
        if (!handleClass.isAssignableFrom(actualClass)) {
            throw new IllegalStateException(String.format(
                    "Incorrect object type expected %s but got %s",
                    handleClass.getName(), actualClass.getName())
            );
        }

        return createProxy(clazz, mapper, handle);
    }

    public final <T extends RemapProxy> T staticRemap(@NonNull Class<T> clazz) {
        ProxyInvocationMapper mapper = mappers.getUnchecked(clazz);
        return createProxy(clazz, mapper, null);
    }

    public final <T extends RemapProxy> T constructRemap(@NonNull Class<T> clazz, @NonNull Object... params) {
        Object[] unwrappedArgs = unwrapArguments(params);
        ProxyInvocationMapper mapper = mappers.getUnchecked(clazz);
        MethodHandle constructor = mapper.findConstructor(
                getParameterTypes(unwrappedArgs)
        );

        Object instance = null;
        try {
            instance = constructor.invokeWithArguments(unwrappedArgs);
        }
        catch (Throwable ex) {
            throw new RuntimeException(String.format(
                    "Failed to construct remap proxy for class '%s': %s",
                    clazz.getName(), ex
            ));
        }
        return remap(clazz, instance);
    }

    Class<?>[] getParameterTypes(Object[] rawArgs) {
        if (rawArgs == null || rawArgs.length == 0) {
            return new Class[0];
        }

        Class<?>[] result = new Class[rawArgs.length];
        for (int i = 0; i < rawArgs.length; i++) {
            Class<?> argClass = rawArgs[i].getClass();
            if (argClass.isPrimitive()) {
                result[i] = argClass;
            }
            else {
                result[i] = TypeUtil.getPrimitiveType(argClass);
            }
        }
        return result;
    }

    Object[] unwrapArguments(Object[] args) {
        if (args == null) {
            return new Object[0];
        }

        if (args.length == 0) {
            return args;
        }

        Object[] result = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            result[i] = unwrapArgument(args[i]);
        }
        return result;
    }

    @Nullable
    Object unwrapArgument(Object arg) {
        if (arg instanceof RemapProxy) {
            return mappers.getUnchecked(((RemapProxy) arg)
                    .getProxyClass())
                    .getHandleClass();
        }
        return arg;
    }

    protected ProxyInvocationMapper loadMapper(Class<? extends RemapProxy> clazz) {
        return new ProxyInvocationMapper(clazz, config);
    }

    private <T extends RemapProxy> T createProxy(Class<T> clazz, ProxyInvocationMapper mapper, Object handle) {
        InvocationHandler handler = new ProxyInvocationHandler(this, mapper, handle);
        return Reflection.newProxy(clazz, handler);
    }
}

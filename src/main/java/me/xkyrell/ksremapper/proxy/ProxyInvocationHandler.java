package me.xkyrell.ksremapper.proxy;

import lombok.RequiredArgsConstructor;
import me.xkyrell.ksremapper.RemapProxy;
import me.xkyrell.ksremapper.accessor.*;
import me.xkyrell.ksremapper.annotation.*;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class ProxyInvocationHandler implements InvocationHandler {

    private static final String GET_PROXY_HANDLE = "getProxyHandle";
    private static final String GET_PROXY_CLASS = "getProxyClass";
    private static final String OBJECT_TOSTRING = "toString";

    private final RemapProxyFactory factory;
    private final ProxyInvocationMapper mapper;
    private final Object handle;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (GET_PROXY_HANDLE.equals(method.getName())) {
            return handle;
        }

        if (GET_PROXY_CLASS.equals(method.getName())) {
            return mapper.getProxyClass();
        }

        if (method.isDefault()) {
            MethodHandle methodHandle = mapper.findDefaultMethod(method);
            return methodHandle.bindTo(proxy).invokeWithArguments(args);
        }

        Object[] unwrapArguments = factory.unwrapArguments(args);
        if (method.getDeclaringClass().equals(Object.class)) {
            MethodHandle methodHandle = mapper.findObjectMethod(method);
            Object value = methodHandle.bindTo(handle).invokeWithArguments(unwrapArguments);
            if (isToStringMethod(method)) {
                return String.format("RemapProxy(name=%s, target=%s)",
                        mapper.getProxyClass().getSimpleName(), value
                );
            }

            return value;
        }

        RemapField remapField = method.getAnnotation(RemapField.class);
        if (remapField != null) {
            if (handle == null && !remapField.isStatic()) {
                throw new IllegalStateException(String.format(
                        "Proxy method '%s' must be marked as isStatic = true for static proxy.",
                        method.getName()
                ));
            }

            FieldAccessor accessor = mapper.findField(method, remapField);
            if (remapField.mode() == RemapField.Mode.GET) {
                if (args != null && args.length > 0) {
                    throw new IllegalArgumentException("The GET operation requires 0 arguments!");
                }

                Object value = accessor.get(handle);
                return proxyOrSource(method, value);
            }

            if (args == null || args.length != 1) {
                throw new IllegalArgumentException("The SET operation requires exactly 1 argument!");
            }

            accessor.set(handle, factory.unwrapArgument(args[0]));
            return null;
        }

        RemapMethod remapMethod = method.getAnnotation(RemapMethod.class);
        if (remapMethod != null) {
            if (handle == null && !remapMethod.isStatic()) {
                throw new IllegalStateException(String.format(
                        "Proxy method '%s' must be marked as isStatic = true for static proxy.",
                        method.getName()
                ));
            }

            Class<?>[] rawTypes = factory.getParameterTypes(unwrapArguments);
            MethodAccessor accessor = mapper.findMethod(method, remapMethod, rawTypes);
            Object value = accessor.invoke(handle, unwrapArguments);
            return proxyOrSource(method, value);
        }

        throw new IllegalStateException(
                "Proxy method " + method + " must have a annotation of either @RemapMethod or @RemapField"
        );
    }

    @SuppressWarnings("unchecked")
    private Object proxyOrSource(Method method, Object value) {
        return (value != null && RemapProxy.class.isAssignableFrom(method.getReturnType()))
                ? factory.remap((Class<? extends RemapProxy>) method.getReturnType(), value)
                : value;
    }

    private boolean isToStringMethod(Method method) {
        return OBJECT_TOSTRING.equals(method.getName())
                && method.getParameterCount() == 0
                && method.getReturnType() == String.class;
    }
}

package me.xkyrell.ksremapper.proxy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.xkyrell.ksremapper.RemapConfig;
import me.xkyrell.ksremapper.RemapProxy;
import java.lang.invoke.MethodHandle;

@RequiredArgsConstructor
public class ProxyInvocationMapper {

    @Getter
    private final Class<? extends RemapProxy> proxyClass;
    private final RemapConfig config;

    // TODO: search for a class and its members

    public MethodHandle findConstructor(Class<?>[] unwrappedTypes) {
        throw new UnsupportedOperationException("Method findConstructor is not implemented");
    }

    public Class<?> getHandleClass() {
        return config.fetchHandle(proxyClass);
    }
}

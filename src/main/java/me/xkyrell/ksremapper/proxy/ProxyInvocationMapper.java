package me.xkyrell.ksremapper.proxy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.xkyrell.ksremapper.RemapProxy;
import java.lang.invoke.MethodHandle;

@Getter
@RequiredArgsConstructor
public class ProxyInvocationMapper {

    private final Class<? extends RemapProxy> proxyClass;
    private final Class<?> handleClass;

    // TODO: search for a class and its members

    public MethodHandle findConstructor(Class<?>[] unwrappedTypes) {
        throw new UnsupportedOperationException("Method findConstructor is not implemented");
    }
}

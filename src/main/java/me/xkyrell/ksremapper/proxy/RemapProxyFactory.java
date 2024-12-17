package me.xkyrell.ksremapper.proxy;

import com.google.common.reflect.Reflection;
import lombok.NonNull;
import me.xkyrell.ksremapper.RemapProxy;
import java.lang.reflect.InvocationHandler;

public class RemapProxyFactory {

    public final <T extends RemapProxy> T remap(@NonNull Class<T> clazz, @NonNull Object handle) {
        return null;
    }

    public final <T extends RemapProxy> T staticRemap(@NonNull Class<T> clazz) {
        return null;
    }

    public final <T extends RemapProxy> T constructRemap(@NonNull Class<T> clazz, @NonNull Object... params) {
        return null;
    }

    private <T extends RemapProxy> T createProxy(Class<T> clazz, ProxyInvocationMapper mapper, Object handle) {
        InvocationHandler handler = new ProxyInvocationHandler(mapper, handle);
        return Reflection.newProxy(clazz, handler);
    }
}

package me.xkyrell.ksremapper.proxy;

import lombok.RequiredArgsConstructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class ProxyInvocationHandler implements InvocationHandler {

    private final ProxyInvocationMapper mapper;
    private final Object handle;

    // TODO: proxy logic

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}

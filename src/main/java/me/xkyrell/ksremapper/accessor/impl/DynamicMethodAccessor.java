package me.xkyrell.ksremapper.accessor.impl;

import me.xkyrell.ksremapper.accessor.AbstractClassMember;
import me.xkyrell.ksremapper.accessor.MethodAccessor;
import me.xkyrell.ksremapper.annotation.RemapMethod;
import me.xkyrell.ksremapper.util.TrustedLookup;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class DynamicMethodAccessor extends AbstractClassMember<MethodType, RemapMethod> implements MethodAccessor {

    public DynamicMethodAccessor(
            Class<?> handleClass, String name, MethodType type, RemapMethod remap
    ) {
        super(handleClass, name, type, remap);
    }

    @Override
    protected MethodHandle prepareAccessor(
            Class<?> handleClass, String name, MethodType type, RemapMethod remap
    ) throws ReflectiveOperationException {
        MethodHandles.Lookup lookup = TrustedLookup.get();
        if (remap.isStatic()) {
            return lookup.findStatic(handleClass, name, type);
        }
        return lookup.findVirtual(handleClass, name, type);
    }

    @Override
    public Object invoke(Object handle, Object[] args) throws Throwable {
        if (!remap.isStatic()) {
            methodHandle = methodHandle.bindTo(handle);
        }
        return methodHandle.invokeWithArguments(args);
    }
}

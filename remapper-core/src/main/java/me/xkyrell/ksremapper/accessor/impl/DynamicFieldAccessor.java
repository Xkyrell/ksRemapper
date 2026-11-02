package me.xkyrell.ksremapper.accessor.impl;

import me.xkyrell.ksremapper.accessor.AbstractClassMember;
import me.xkyrell.ksremapper.accessor.FieldAccessor;
import me.xkyrell.ksremapper.annotation.RemapField;
import me.xkyrell.ksremapper.util.TrustedLookup;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

public class DynamicFieldAccessor extends AbstractClassMember<Class<?>, RemapField> implements FieldAccessor {

    public DynamicFieldAccessor(
            Class<?> handleClass, String name, Class<?> type, RemapField remap
    ) {
        super(handleClass, name, type, remap);
    }

    @Override
    protected MethodHandle prepareAccessor(
            Class<?> handleClass, String name,
            Class<?> type, RemapField remap
    ) throws ReflectiveOperationException {
        MethodHandles.Lookup lookup = TrustedLookup.get();
        if (remap.mode().equals(RemapField.Mode.GET)) {
            return remap.isStatic()
                    ? lookup.findStaticGetter(handleClass, name, type)
                    : lookup.findGetter(handleClass, name, type);
        }
        return remap.isStatic()
                ? lookup.findStaticSetter(handleClass, name, type)
                : lookup.findSetter(handleClass, name, type);
    }

    @Override
    public Object get(Object handle) throws Throwable {
        return remap.isStatic() ? methodHandle.invoke() : methodHandle.invoke(handle);
    }

    @Override
    public void set(Object handle, Object value) throws Throwable {
        if (!remap.isStatic()) {
            methodHandle.invoke(handle, value);
            return;
        }
        methodHandle.invoke(value);
    }
}

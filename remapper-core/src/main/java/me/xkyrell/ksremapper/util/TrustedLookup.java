package me.xkyrell.ksremapper.util;

import lombok.experimental.UtilityClass;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.function.Function;

@UtilityClass
public class TrustedLookup {

    private static final MethodHandles.Lookup INSTANCE;

    static {
        try {
            Field field = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
            field.setAccessible(true);

            INSTANCE = (MethodHandles.Lookup) field.get(null);
        }
        catch (NoSuchFieldException | IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public <T> T apply(Function<MethodHandles.Lookup, ? extends T> function) {
        return function.apply(INSTANCE);
    }

    public MethodHandles.Lookup get() {
        return INSTANCE;
    }
}

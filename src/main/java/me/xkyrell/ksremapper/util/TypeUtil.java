package me.xkyrell.ksremapper.util;

import lombok.experimental.UtilityClass;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class TypeUtil {

    private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE = new HashMap<>(8);

    static {
        WRAPPER_TO_PRIMITIVE.put(Boolean.class, boolean.class);
        WRAPPER_TO_PRIMITIVE.put(Byte.class, byte.class);
        WRAPPER_TO_PRIMITIVE.put(Character.class, char.class);
        WRAPPER_TO_PRIMITIVE.put(Short.class, short.class);
        WRAPPER_TO_PRIMITIVE.put(Integer.class, int.class);
        WRAPPER_TO_PRIMITIVE.put(Long.class, long.class);
        WRAPPER_TO_PRIMITIVE.put(Float.class, float.class);
        WRAPPER_TO_PRIMITIVE.put(Double.class, double.class);
    }

    public static Class<?> getPrimitiveType(Class<?> wrapperType) {
        return WRAPPER_TO_PRIMITIVE.getOrDefault(wrapperType, wrapperType);
    }
}

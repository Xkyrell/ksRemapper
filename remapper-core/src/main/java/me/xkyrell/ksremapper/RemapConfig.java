package me.xkyrell.ksremapper;

import lombok.NonNull;
import me.xkyrell.ksremapper.util.Buildable;
import java.lang.reflect.Method;
import java.util.function.Function;

public interface RemapConfig {

    static Builder builder() {
        return new RemapConfigBase.SimpleBuilder();
    }

    Class<?> fetchHandle(@NonNull Class<? extends RemapProxy> clazz);

    String fetchMember(@NonNull Method method);

    interface Builder extends Buildable<RemapConfig> {

        Builder targetHandler(Function<Class<? extends RemapProxy>, Class<?>> targetHandler);

        Builder classHandler(Function<Class<? extends RemapProxy>, Class<?>> classHandler);

        Builder memberHandler(Function<Method, String> targetMember);

    }
}

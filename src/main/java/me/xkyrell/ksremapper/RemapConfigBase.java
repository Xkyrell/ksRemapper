package me.xkyrell.ksremapper;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.xkyrell.ksremapper.annotation.RemapClass;
import me.xkyrell.ksremapper.annotation.RemapTarget;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
final class RemapConfigBase implements RemapConfig {

    private final Function<Class<? extends RemapProxy>, Class<?>> targetHandler;
    private final Function<Class<? extends RemapProxy>, Class<?>> classHandler;
    private final Function<Method, String> memberHandler;

    @Override
    public Class<?> fetchHandle(@NonNull Class<? extends RemapProxy> clazz) {
        try {
            return targetHandler.apply(clazz);
        }
        catch (Throwable __) {
            return classHandler.apply(clazz);
        }
    }

    @Override
    public String fetchMember(@NonNull Method method) {
        return memberHandler.apply(method);
    }

    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    static final class SimpleBuilder implements RemapConfig.Builder {

        private Function<Class<? extends RemapProxy>, Class<?>> targetHandler = clazz -> {
            try {
                RemapTarget target = clazz.getAnnotation(RemapTarget.class);
                return Class.forName(target.value());
            }
            catch (ClassNotFoundException ex) {
                throw new IllegalStateException(ex);
            }
        };

        private Function<Class<? extends RemapProxy>, Class<?>> classHandler = clazz ->
                Optional.ofNullable(clazz.getAnnotation(RemapClass.class))
                        .map(RemapClass::value)
                        .orElseThrow(() -> new IllegalStateException(
                                "The proxy class " + clazz.getName() + " does not have an assigned handle class."
                        ));

        private Function<Method, String> memberHandler = method ->
                Optional.ofNullable(method.getAnnotation(RemapTarget.class))
                        .map(RemapTarget::value)
                        .filter(value -> !value.isEmpty())
                        .orElse(method.getName());

        @Override
        public Builder targetHandler(Function<Class<? extends RemapProxy>, Class<?>> targetHandler) {
            this.targetHandler = targetHandler;
            return this;
        }

        @Override
        public Builder classHandler(Function<Class<? extends RemapProxy>, Class<?>> classHandler) {
            this.classHandler = classHandler;
            return this;
        }

        @Override
        public Builder memberHandler(Function<Method, String> memberHandler) {
            this.memberHandler = memberHandler;
            return this;
        }

        @Override
        public RemapConfig build() {
            Preconditions.checkNotNull(targetHandler, "targetBinder cannot be null");
            Preconditions.checkNotNull(classHandler, "classBinder cannot be null");
            Preconditions.checkNotNull(memberHandler, "targetMember cannot be null");

            return new RemapConfigBase(targetHandler, classHandler, memberHandler);
        }
    }
}

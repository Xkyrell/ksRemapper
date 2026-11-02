package me.xkyrell.ksremapper.accessor;

public interface MethodAccessor {

    Object invoke(Object handle, Object[] args) throws Throwable;

}

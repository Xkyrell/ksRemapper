package me.xkyrell.ksremapper.accessor;

public interface FieldAccessor {

    Object get(Object handle) throws Throwable;

    void set(Object handle, Object value) throws Throwable;

}

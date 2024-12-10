package me.xkyrell.ksremapper;

public interface RemapProxy {

    Class<? extends RemapProxy> getProxyClass();

    Object getHandle();

}
package me.xkyrell.ksremapper;

import me.xkyrell.ksremapper.proxy.RemapProxyFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StaticRemapTest {

    private static final RemapProxyFactory FACTORY = new RemapProxyFactory();

    @Test
    public void testStaticProxy() {
        UserProxy staticProxy = FACTORY.staticRemap(UserProxy.class);

        staticProxy.notifyCrash();

        assertEquals(111, staticProxy.ID());
        assertThrows(IllegalStateException.class, staticProxy::getName);
    }

    @Test
    public void testProxyWithStatic() {
        UserProxy proxy = FACTORY.remap(UserProxy.class, new User("Markus"));
        assertDoesNotThrow(proxy::notifyCrash);
    }
}

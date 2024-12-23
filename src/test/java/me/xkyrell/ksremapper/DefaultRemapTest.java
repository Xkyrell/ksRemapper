package me.xkyrell.ksremapper;

import me.xkyrell.ksremapper.proxy.RemapProxyFactory;
import org.junit.jupiter.api.Test;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultRemapTest {

    private static final RemapProxyFactory FACTORY = new RemapProxyFactory();

    @Test
    public void testProxy() {
        User user = new User("Steven");
        UserProxy proxy = FACTORY.remap(UserProxy.class, user);
        assertEquals(user.getName(), proxy.getName());

        assertEquals("Rookie", user.getTag());
        assertEquals("Rookie", proxy.getTag());
        proxy.setTag("Veteran");
        assertEquals("Veteran", user.getTag());
        assertEquals("Veteran", proxy.getTag());

        assertEquals("Steven", user.getName());
        assertEquals("Steven", proxy.getName());
        proxy.name("Robert");
        assertEquals("Robert", user.getName());
        assertEquals("Robert", proxy.getName());

        assertEquals(proxy.getJoined(), user.getJoined());
        proxy.setDate(new Date(0));
        assertEquals(user.getJoined(), proxy.getJoined());
    }

    @Test
    public void testProxyObjectMethods() {
        User user = new User("Steven");
        UserProxy proxy = FACTORY.remap(UserProxy.class, user);

        assertEquals(user.hashCode(), proxy.hashCode());
        assertNotSame(user.toString(), proxy.toString());

        User proxyUser = (User) proxy.getProxyHandle();
        assertNotNull(proxyUser);
        assertEquals(user, proxyUser);
    }

    @Test
    public void testProxyConstruct() {
        long time = 456764320L;
        UserProxy proxy = FACTORY.constructRemap(UserProxy.class,
                "Olivia", new Date(time), "Master", 95000
        );

        assertEquals("Olivia", proxy.getName());
        assertEquals(time, proxy.getJoined().getTime());
        assertEquals("Master", proxy.getTag());

        assertEquals(95000, proxy.getPoints());
        User user = (User) proxy.getProxyHandle();
        user.incrementPoints();
        assertEquals(95001, proxy.getPoints());

        proxy.setUnlimitedPoints();
        assertEquals(Integer.MAX_VALUE, proxy.getPoints());
    }
}

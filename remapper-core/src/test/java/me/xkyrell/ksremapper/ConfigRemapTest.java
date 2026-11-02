package me.xkyrell.ksremapper;

import me.xkyrell.ksremapper.annotation.RemapField;
import me.xkyrell.ksremapper.annotation.RemapTarget;
import me.xkyrell.ksremapper.proxy.RemapProxyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigRemapTest {

    private RemapProxyFactory factory;

    @BeforeEach
    public void setUp() {
        RemapConfig.Builder builder = RemapConfig.builder().classHandler(clazz -> {
            try {
                RemapTarget target = clazz.getAnnotation(RemapTarget.class);
                return Class.forName("me.xkyrell.ksremapper.ConfigRemapTest$" + target.value());
            }
            catch (ClassNotFoundException ex) {
                throw new IllegalStateException(ex);
            }
        });

        factory = new RemapProxyFactory(builder.build());
    }

    @Test
    public void testProxyWithCustom() {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.addUser(new User("Tom"));
        dispatcher.addUser(new User("Nick"));
        dispatcher.addUser(new User("Ria"));
        assertEquals(3, dispatcher.getTotalUsers());

        DispatcherProxy proxy = factory.remap(DispatcherProxy.class, dispatcher);
        proxy.users().forEach(System.out::println);
        proxy.users().clear();

        assertEquals(0, dispatcher.getTotalUsers());
    }

    @RemapTarget("Dispatcher")
    private interface DispatcherProxy extends RemapProxy {

        @RemapField(mode = RemapField.Mode.GET)
        List<User> users();

    }

    private static final class Dispatcher {

        private final List<User> users = new ArrayList<>();

        public void addUser(User user) {
            users.add(user);
        }

        public int getTotalUsers() {
            return users.size();
        }
    }
}

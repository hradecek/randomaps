package com.hradecek.maps;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.Assert;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Testing utility functions for assertions.
 */
public class AssertionsUtils {

    private AssertionsUtils() {
        throw new AssertionError("Cannot instantiate");
    }

    public static <T> void assertPrivateConstructor(Class<T> clazz) {
        try {
            final var constructor = clazz.getDeclaredConstructor();
            assertThat(Modifier.isPrivate(constructor.getModifiers()), is(true));
            constructor.setAccessible(true);
            final var exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
            assertThat(exception.getCause().getClass(), equalTo(AssertionError.class));
        } catch (NoSuchMethodException ex) {
            Assert.fail("No constructor has been declared");
        }
    }
}

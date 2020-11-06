package com.hradecek.maps.utils;

import org.junit.jupiter.api.Test;

import static com.hradecek.maps.AssertionsUtils.assertPrivateConstructor;

/**
 * Base class for unit tests testings utility classes.
 * <p>
 * All utility classes must pass unit test presented in this class.
 */
public abstract class UtilsClassUnitTest<T> {

    @Test
    public void cannotInstantiate() {
        assertPrivateConstructor(getUtilityClass());
    }

    protected abstract Class<T> getUtilityClass();
}

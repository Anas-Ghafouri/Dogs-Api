package com.polaris;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import jakarta.inject.Inject;

@MicronautTest
class DogsApiTest {

    @Inject
    EmbeddedApplication<?> application;

    @Test
    void testApplicationStarts() {
        Assertions.assertTrue(application.isRunning());
    }

}

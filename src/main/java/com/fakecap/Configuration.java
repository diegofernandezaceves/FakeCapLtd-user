package com.fakecap;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import net.datafaker.Faker;

@ApplicationScoped
public class Configuration {

    @Produces
    public Faker faker() {
        return new Faker();
    }

}

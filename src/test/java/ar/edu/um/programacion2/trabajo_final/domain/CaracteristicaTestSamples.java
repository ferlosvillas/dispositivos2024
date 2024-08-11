package ar.edu.um.programacion2.trabajo_final.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CaracteristicaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Caracteristica getCaracteristicaSample1() {
        return new Caracteristica().id(1L).nombre("nombre1").descripcion("descripcion1");
    }

    public static Caracteristica getCaracteristicaSample2() {
        return new Caracteristica().id(2L).nombre("nombre2").descripcion("descripcion2");
    }

    public static Caracteristica getCaracteristicaRandomSampleGenerator() {
        return new Caracteristica()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}

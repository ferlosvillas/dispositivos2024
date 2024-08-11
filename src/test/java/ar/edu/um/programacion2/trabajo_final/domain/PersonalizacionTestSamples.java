package ar.edu.um.programacion2.trabajo_final.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PersonalizacionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Personalizacion getPersonalizacionSample1() {
        return new Personalizacion().id(1L).nombre("nombre1").descripcion("descripcion1");
    }

    public static Personalizacion getPersonalizacionSample2() {
        return new Personalizacion().id(2L).nombre("nombre2").descripcion("descripcion2");
    }

    public static Personalizacion getPersonalizacionRandomSampleGenerator() {
        return new Personalizacion()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}

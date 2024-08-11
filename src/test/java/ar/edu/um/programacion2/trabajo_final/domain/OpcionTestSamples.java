package ar.edu.um.programacion2.trabajo_final.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OpcionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Opcion getOpcionSample1() {
        return new Opcion().id(1L).codigo("codigo1").nombre("nombre1").descripcion("descripcion1");
    }

    public static Opcion getOpcionSample2() {
        return new Opcion().id(2L).codigo("codigo2").nombre("nombre2").descripcion("descripcion2");
    }

    public static Opcion getOpcionRandomSampleGenerator() {
        return new Opcion()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}

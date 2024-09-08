package ar.edu.um.programacion2.trabajo_final.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VentaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Venta getVentaSample1() {
        return new Venta().id(1L).idVenta(1L).codigo("codigo1").nombre("nombre1").descripcion("descripcion1");
    }

    public static Venta getVentaSample2() {
        return new Venta().id(2L).idVenta(2L).codigo("codigo2").nombre("nombre2").descripcion("descripcion2");
    }

    public static Venta getVentaRandomSampleGenerator() {
        return new Venta()
            .id(longCount.incrementAndGet())
            .idVenta(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}

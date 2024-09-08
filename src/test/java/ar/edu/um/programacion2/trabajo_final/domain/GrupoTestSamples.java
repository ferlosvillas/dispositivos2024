package ar.edu.um.programacion2.trabajo_final.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GrupoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Grupo getGrupoSample1() {
        return new Grupo()
            .id(1L)
            .idGrupo(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .nombres("nombres1")
            .descripcion("descripcion1");
    }

    public static Grupo getGrupoSample2() {
        return new Grupo()
            .id(2L)
            .idGrupo(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .nombres("nombres2")
            .descripcion("descripcion2");
    }

    public static Grupo getGrupoRandomSampleGenerator() {
        return new Grupo()
            .id(longCount.incrementAndGet())
            .idGrupo(UUID.randomUUID())
            .nombres(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}

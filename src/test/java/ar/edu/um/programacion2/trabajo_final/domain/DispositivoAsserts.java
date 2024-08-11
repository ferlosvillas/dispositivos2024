package ar.edu.um.programacion2.trabajo_final.domain;

import static ar.edu.um.programacion2.trabajo_final.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class DispositivoAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDispositivoAllPropertiesEquals(Dispositivo expected, Dispositivo actual) {
        assertDispositivoAutoGeneratedPropertiesEquals(expected, actual);
        assertDispositivoAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDispositivoAllUpdatablePropertiesEquals(Dispositivo expected, Dispositivo actual) {
        assertDispositivoUpdatableFieldsEquals(expected, actual);
        assertDispositivoUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDispositivoAutoGeneratedPropertiesEquals(Dispositivo expected, Dispositivo actual) {
        assertThat(expected)
            .as("Verify Dispositivo auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDispositivoUpdatableFieldsEquals(Dispositivo expected, Dispositivo actual) {
        assertThat(expected)
            .as("Verify Dispositivo relevant properties")
            .satisfies(e -> assertThat(e.getCodigo()).as("check codigo").isEqualTo(actual.getCodigo()))
            .satisfies(e -> assertThat(e.getNombre()).as("check nombre").isEqualTo(actual.getNombre()))
            .satisfies(e -> assertThat(e.getDescripcion()).as("check descripcion").isEqualTo(actual.getDescripcion()))
            .satisfies(
                e ->
                    assertThat(e.getPrecioBase())
                        .as("check precioBase")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getPrecioBase())
            )
            .satisfies(e -> assertThat(e.getMoneda()).as("check moneda").isEqualTo(actual.getMoneda()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDispositivoUpdatableRelationshipsEquals(Dispositivo expected, Dispositivo actual) {
        assertThat(expected)
            .as("Verify Dispositivo relationships")
            .satisfies(e -> assertThat(e.getPersonalizaciones()).as("check personalizaciones").isEqualTo(actual.getPersonalizaciones()))
            .satisfies(e -> assertThat(e.getAdicionales()).as("check adicionales").isEqualTo(actual.getAdicionales()));
    }
}

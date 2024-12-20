package ar.edu.um.programacion2.trabajo_final.domain;

import static ar.edu.um.programacion2.trabajo_final.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class VentaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVentaAllPropertiesEquals(Venta expected, Venta actual) {
        assertVentaAutoGeneratedPropertiesEquals(expected, actual);
        assertVentaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVentaAllUpdatablePropertiesEquals(Venta expected, Venta actual) {
        assertVentaUpdatableFieldsEquals(expected, actual);
        assertVentaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVentaAutoGeneratedPropertiesEquals(Venta expected, Venta actual) {
        assertThat(expected)
            .as("Verify Venta auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVentaUpdatableFieldsEquals(Venta expected, Venta actual) {
        assertThat(expected)
            .as("Verify Venta relevant properties")
            .satisfies(e -> assertThat(e.getIdVenta()).as("check idVenta").isEqualTo(actual.getIdVenta()))
            .satisfies(e -> assertThat(e.getCodigo()).as("check codigo").isEqualTo(actual.getCodigo()))
            .satisfies(e -> assertThat(e.getNombre()).as("check nombre").isEqualTo(actual.getNombre()))
            .satisfies(e -> assertThat(e.getDescripcion()).as("check descripcion").isEqualTo(actual.getDescripcion()))
            .satisfies(e -> assertThat(e.getPrecio()).as("check precio").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getPrecio()))
            .satisfies(e -> assertThat(e.getVentaPedidoJson()).as("check ventaPedidoJson").isEqualTo(actual.getVentaPedidoJson()))
            .satisfies(e -> assertThat(e.getVentaResultadoJson()).as("check ventaResultadoJson").isEqualTo(actual.getVentaResultadoJson()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVentaUpdatableRelationshipsEquals(Venta expected, Venta actual) {
        assertThat(expected)
            .as("Verify Venta relationships")
            .satisfies(e -> assertThat(e.getGrupo()).as("check grupo").isEqualTo(actual.getGrupo()));
    }
}

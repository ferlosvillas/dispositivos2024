package ar.edu.um.programacion2.trabajo_final.domain;

import static ar.edu.um.programacion2.trabajo_final.domain.GrupoTestSamples.*;
import static ar.edu.um.programacion2.trabajo_final.domain.VentaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.trabajo_final.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VentaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Venta.class);
        Venta venta1 = getVentaSample1();
        Venta venta2 = new Venta();
        assertThat(venta1).isNotEqualTo(venta2);

        venta2.setId(venta1.getId());
        assertThat(venta1).isEqualTo(venta2);

        venta2 = getVentaSample2();
        assertThat(venta1).isNotEqualTo(venta2);
    }

    @Test
    void grupoTest() {
        Venta venta = getVentaRandomSampleGenerator();
        Grupo grupoBack = getGrupoRandomSampleGenerator();

        venta.setGrupo(grupoBack);
        assertThat(venta.getGrupo()).isEqualTo(grupoBack);

        venta.grupo(null);
        assertThat(venta.getGrupo()).isNull();
    }
}

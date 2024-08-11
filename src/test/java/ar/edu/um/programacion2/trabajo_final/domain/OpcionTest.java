package ar.edu.um.programacion2.trabajo_final.domain;

import static ar.edu.um.programacion2.trabajo_final.domain.OpcionTestSamples.*;
import static ar.edu.um.programacion2.trabajo_final.domain.PersonalizacionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.trabajo_final.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OpcionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Opcion.class);
        Opcion opcion1 = getOpcionSample1();
        Opcion opcion2 = new Opcion();
        assertThat(opcion1).isNotEqualTo(opcion2);

        opcion2.setId(opcion1.getId());
        assertThat(opcion1).isEqualTo(opcion2);

        opcion2 = getOpcionSample2();
        assertThat(opcion1).isNotEqualTo(opcion2);
    }

    @Test
    void personlaizacionTest() {
        Opcion opcion = getOpcionRandomSampleGenerator();
        Personalizacion personalizacionBack = getPersonalizacionRandomSampleGenerator();

        opcion.setPersonlaizacion(personalizacionBack);
        assertThat(opcion.getPersonlaizacion()).isEqualTo(personalizacionBack);

        opcion.personlaizacion(null);
        assertThat(opcion.getPersonlaizacion()).isNull();
    }
}

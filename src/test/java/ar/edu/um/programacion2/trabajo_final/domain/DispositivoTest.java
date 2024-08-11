package ar.edu.um.programacion2.trabajo_final.domain;

import static ar.edu.um.programacion2.trabajo_final.domain.AdicionalTestSamples.*;
import static ar.edu.um.programacion2.trabajo_final.domain.CaracteristicaTestSamples.*;
import static ar.edu.um.programacion2.trabajo_final.domain.DispositivoTestSamples.*;
import static ar.edu.um.programacion2.trabajo_final.domain.PersonalizacionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.trabajo_final.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DispositivoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dispositivo.class);
        Dispositivo dispositivo1 = getDispositivoSample1();
        Dispositivo dispositivo2 = new Dispositivo();
        assertThat(dispositivo1).isNotEqualTo(dispositivo2);

        dispositivo2.setId(dispositivo1.getId());
        assertThat(dispositivo1).isEqualTo(dispositivo2);

        dispositivo2 = getDispositivoSample2();
        assertThat(dispositivo1).isNotEqualTo(dispositivo2);
    }

    @Test
    void caracteristicasTest() {
        Dispositivo dispositivo = getDispositivoRandomSampleGenerator();
        Caracteristica caracteristicaBack = getCaracteristicaRandomSampleGenerator();

        dispositivo.addCaracteristicas(caracteristicaBack);
        assertThat(dispositivo.getCaracteristicas()).containsOnly(caracteristicaBack);
        assertThat(caracteristicaBack.getDispositivo()).isEqualTo(dispositivo);

        dispositivo.removeCaracteristicas(caracteristicaBack);
        assertThat(dispositivo.getCaracteristicas()).doesNotContain(caracteristicaBack);
        assertThat(caracteristicaBack.getDispositivo()).isNull();

        dispositivo.caracteristicas(new HashSet<>(Set.of(caracteristicaBack)));
        assertThat(dispositivo.getCaracteristicas()).containsOnly(caracteristicaBack);
        assertThat(caracteristicaBack.getDispositivo()).isEqualTo(dispositivo);

        dispositivo.setCaracteristicas(new HashSet<>());
        assertThat(dispositivo.getCaracteristicas()).doesNotContain(caracteristicaBack);
        assertThat(caracteristicaBack.getDispositivo()).isNull();
    }

    @Test
    void personalizacionesTest() {
        Dispositivo dispositivo = getDispositivoRandomSampleGenerator();
        Personalizacion personalizacionBack = getPersonalizacionRandomSampleGenerator();

        dispositivo.addPersonalizaciones(personalizacionBack);
        assertThat(dispositivo.getPersonalizaciones()).containsOnly(personalizacionBack);

        dispositivo.removePersonalizaciones(personalizacionBack);
        assertThat(dispositivo.getPersonalizaciones()).doesNotContain(personalizacionBack);

        dispositivo.personalizaciones(new HashSet<>(Set.of(personalizacionBack)));
        assertThat(dispositivo.getPersonalizaciones()).containsOnly(personalizacionBack);

        dispositivo.setPersonalizaciones(new HashSet<>());
        assertThat(dispositivo.getPersonalizaciones()).doesNotContain(personalizacionBack);
    }

    @Test
    void adicionalesTest() {
        Dispositivo dispositivo = getDispositivoRandomSampleGenerator();
        Adicional adicionalBack = getAdicionalRandomSampleGenerator();

        dispositivo.addAdicionales(adicionalBack);
        assertThat(dispositivo.getAdicionales()).containsOnly(adicionalBack);

        dispositivo.removeAdicionales(adicionalBack);
        assertThat(dispositivo.getAdicionales()).doesNotContain(adicionalBack);

        dispositivo.adicionales(new HashSet<>(Set.of(adicionalBack)));
        assertThat(dispositivo.getAdicionales()).containsOnly(adicionalBack);

        dispositivo.setAdicionales(new HashSet<>());
        assertThat(dispositivo.getAdicionales()).doesNotContain(adicionalBack);
    }
}

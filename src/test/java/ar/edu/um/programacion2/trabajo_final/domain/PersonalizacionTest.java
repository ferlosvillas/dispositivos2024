package ar.edu.um.programacion2.trabajo_final.domain;

import static ar.edu.um.programacion2.trabajo_final.domain.DispositivoTestSamples.*;
import static ar.edu.um.programacion2.trabajo_final.domain.OpcionTestSamples.*;
import static ar.edu.um.programacion2.trabajo_final.domain.PersonalizacionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.trabajo_final.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PersonalizacionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Personalizacion.class);
        Personalizacion personalizacion1 = getPersonalizacionSample1();
        Personalizacion personalizacion2 = new Personalizacion();
        assertThat(personalizacion1).isNotEqualTo(personalizacion2);

        personalizacion2.setId(personalizacion1.getId());
        assertThat(personalizacion1).isEqualTo(personalizacion2);

        personalizacion2 = getPersonalizacionSample2();
        assertThat(personalizacion1).isNotEqualTo(personalizacion2);
    }

    @Test
    void opcionesTest() {
        Personalizacion personalizacion = getPersonalizacionRandomSampleGenerator();
        Opcion opcionBack = getOpcionRandomSampleGenerator();

        personalizacion.addOpciones(opcionBack);
        assertThat(personalizacion.getOpciones()).containsOnly(opcionBack);
        assertThat(opcionBack.getPersonalizacion()).isEqualTo(personalizacion);

        personalizacion.removeOpciones(opcionBack);
        assertThat(personalizacion.getOpciones()).doesNotContain(opcionBack);
        assertThat(opcionBack.getPersonalizacion()).isNull();

        personalizacion.opciones(new HashSet<>(Set.of(opcionBack)));
        assertThat(personalizacion.getOpciones()).containsOnly(opcionBack);
        assertThat(opcionBack.getPersonalizacion()).isEqualTo(personalizacion);

        personalizacion.setOpciones(new HashSet<>());
        assertThat(personalizacion.getOpciones()).doesNotContain(opcionBack);
        assertThat(opcionBack.getPersonalizacion()).isNull();
    }

    @Test
    void dispositivosTest() {
        Personalizacion personalizacion = getPersonalizacionRandomSampleGenerator();
        Dispositivo dispositivoBack = getDispositivoRandomSampleGenerator();

        personalizacion.addDispositivos(dispositivoBack);
        assertThat(personalizacion.getDispositivos()).containsOnly(dispositivoBack);
        assertThat(dispositivoBack.getPersonalizaciones()).containsOnly(personalizacion);

        personalizacion.removeDispositivos(dispositivoBack);
        assertThat(personalizacion.getDispositivos()).doesNotContain(dispositivoBack);
        assertThat(dispositivoBack.getPersonalizaciones()).doesNotContain(personalizacion);

        personalizacion.dispositivos(new HashSet<>(Set.of(dispositivoBack)));
        assertThat(personalizacion.getDispositivos()).containsOnly(dispositivoBack);
        assertThat(dispositivoBack.getPersonalizaciones()).containsOnly(personalizacion);

        personalizacion.setDispositivos(new HashSet<>());
        assertThat(personalizacion.getDispositivos()).doesNotContain(dispositivoBack);
        assertThat(dispositivoBack.getPersonalizaciones()).doesNotContain(personalizacion);
    }
}

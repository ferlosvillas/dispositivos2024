package ar.edu.um.programacion2.trabajo_final.domain;

import static ar.edu.um.programacion2.trabajo_final.domain.GrupoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.trabajo_final.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GrupoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Grupo.class);
        Grupo grupo1 = getGrupoSample1();
        Grupo grupo2 = new Grupo();
        assertThat(grupo1).isNotEqualTo(grupo2);

        grupo2.setId(grupo1.getId());
        assertThat(grupo1).isEqualTo(grupo2);

        grupo2 = getGrupoSample2();
        assertThat(grupo1).isNotEqualTo(grupo2);
    }
}

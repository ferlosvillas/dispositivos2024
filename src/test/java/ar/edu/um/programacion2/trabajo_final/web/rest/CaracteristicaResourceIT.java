package ar.edu.um.programacion2.trabajo_final.web.rest;

import static ar.edu.um.programacion2.trabajo_final.domain.CaracteristicaAsserts.*;
import static ar.edu.um.programacion2.trabajo_final.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.trabajo_final.IntegrationTest;
import ar.edu.um.programacion2.trabajo_final.domain.Caracteristica;
import ar.edu.um.programacion2.trabajo_final.repository.CaracteristicaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CaracteristicaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CaracteristicaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/caracteristicas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCaracteristicaMockMvc;

    private Caracteristica caracteristica;

    private Caracteristica insertedCaracteristica;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caracteristica createEntity(EntityManager em) {
        Caracteristica caracteristica = new Caracteristica().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION);
        return caracteristica;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caracteristica createUpdatedEntity(EntityManager em) {
        Caracteristica caracteristica = new Caracteristica().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        return caracteristica;
    }

    @BeforeEach
    public void initTest() {
        caracteristica = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCaracteristica != null) {
            caracteristicaRepository.delete(insertedCaracteristica);
            insertedCaracteristica = null;
        }
    }

    @Test
    @Transactional
    void createCaracteristica() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Caracteristica
        var returnedCaracteristica = om.readValue(
            restCaracteristicaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(caracteristica)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Caracteristica.class
        );

        // Validate the Caracteristica in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCaracteristicaUpdatableFieldsEquals(returnedCaracteristica, getPersistedCaracteristica(returnedCaracteristica));

        insertedCaracteristica = returnedCaracteristica;
    }

    @Test
    @Transactional
    void createCaracteristicaWithExistingId() throws Exception {
        // Create the Caracteristica with an existing ID
        caracteristica.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCaracteristicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(caracteristica)))
            .andExpect(status().isBadRequest());

        // Validate the Caracteristica in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCaracteristicas() throws Exception {
        // Initialize the database
        insertedCaracteristica = caracteristicaRepository.saveAndFlush(caracteristica);

        // Get all the caracteristicaList
        restCaracteristicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caracteristica.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getCaracteristica() throws Exception {
        // Initialize the database
        insertedCaracteristica = caracteristicaRepository.saveAndFlush(caracteristica);

        // Get the caracteristica
        restCaracteristicaMockMvc
            .perform(get(ENTITY_API_URL_ID, caracteristica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(caracteristica.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingCaracteristica() throws Exception {
        // Get the caracteristica
        restCaracteristicaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCaracteristica() throws Exception {
        // Initialize the database
        insertedCaracteristica = caracteristicaRepository.saveAndFlush(caracteristica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the caracteristica
        Caracteristica updatedCaracteristica = caracteristicaRepository.findById(caracteristica.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCaracteristica are not directly saved in db
        em.detach(updatedCaracteristica);
        updatedCaracteristica.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restCaracteristicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCaracteristica.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCaracteristica))
            )
            .andExpect(status().isOk());

        // Validate the Caracteristica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCaracteristicaToMatchAllProperties(updatedCaracteristica);
    }

    @Test
    @Transactional
    void putNonExistingCaracteristica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caracteristica.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaracteristicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, caracteristica.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(caracteristica))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caracteristica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCaracteristica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caracteristica.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaracteristicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(caracteristica))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caracteristica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCaracteristica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caracteristica.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaracteristicaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(caracteristica)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Caracteristica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCaracteristicaWithPatch() throws Exception {
        // Initialize the database
        insertedCaracteristica = caracteristicaRepository.saveAndFlush(caracteristica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the caracteristica using partial update
        Caracteristica partialUpdatedCaracteristica = new Caracteristica();
        partialUpdatedCaracteristica.setId(caracteristica.getId());

        restCaracteristicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaracteristica.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCaracteristica))
            )
            .andExpect(status().isOk());

        // Validate the Caracteristica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCaracteristicaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCaracteristica, caracteristica),
            getPersistedCaracteristica(caracteristica)
        );
    }

    @Test
    @Transactional
    void fullUpdateCaracteristicaWithPatch() throws Exception {
        // Initialize the database
        insertedCaracteristica = caracteristicaRepository.saveAndFlush(caracteristica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the caracteristica using partial update
        Caracteristica partialUpdatedCaracteristica = new Caracteristica();
        partialUpdatedCaracteristica.setId(caracteristica.getId());

        partialUpdatedCaracteristica.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restCaracteristicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaracteristica.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCaracteristica))
            )
            .andExpect(status().isOk());

        // Validate the Caracteristica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCaracteristicaUpdatableFieldsEquals(partialUpdatedCaracteristica, getPersistedCaracteristica(partialUpdatedCaracteristica));
    }

    @Test
    @Transactional
    void patchNonExistingCaracteristica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caracteristica.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaracteristicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, caracteristica.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(caracteristica))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caracteristica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCaracteristica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caracteristica.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaracteristicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(caracteristica))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caracteristica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCaracteristica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caracteristica.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaracteristicaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(caracteristica)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Caracteristica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCaracteristica() throws Exception {
        // Initialize the database
        insertedCaracteristica = caracteristicaRepository.saveAndFlush(caracteristica);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the caracteristica
        restCaracteristicaMockMvc
            .perform(delete(ENTITY_API_URL_ID, caracteristica.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return caracteristicaRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Caracteristica getPersistedCaracteristica(Caracteristica caracteristica) {
        return caracteristicaRepository.findById(caracteristica.getId()).orElseThrow();
    }

    protected void assertPersistedCaracteristicaToMatchAllProperties(Caracteristica expectedCaracteristica) {
        assertCaracteristicaAllPropertiesEquals(expectedCaracteristica, getPersistedCaracteristica(expectedCaracteristica));
    }

    protected void assertPersistedCaracteristicaToMatchUpdatableProperties(Caracteristica expectedCaracteristica) {
        assertCaracteristicaAllUpdatablePropertiesEquals(expectedCaracteristica, getPersistedCaracteristica(expectedCaracteristica));
    }
}

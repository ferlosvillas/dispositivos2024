package ar.edu.um.programacion2.trabajo_final.web.rest;

import static ar.edu.um.programacion2.trabajo_final.domain.PersonalizacionAsserts.*;
import static ar.edu.um.programacion2.trabajo_final.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.trabajo_final.IntegrationTest;
import ar.edu.um.programacion2.trabajo_final.domain.Personalizacion;
import ar.edu.um.programacion2.trabajo_final.repository.PersonalizacionRepository;
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
 * Integration tests for the {@link PersonalizacionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonalizacionResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/personalizacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PersonalizacionRepository personalizacionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonalizacionMockMvc;

    private Personalizacion personalizacion;

    private Personalizacion insertedPersonalizacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personalizacion createEntity(EntityManager em) {
        Personalizacion personalizacion = new Personalizacion().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION);
        return personalizacion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personalizacion createUpdatedEntity(EntityManager em) {
        Personalizacion personalizacion = new Personalizacion().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        return personalizacion;
    }

    @BeforeEach
    public void initTest() {
        personalizacion = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPersonalizacion != null) {
            personalizacionRepository.delete(insertedPersonalizacion);
            insertedPersonalizacion = null;
        }
    }

    @Test
    @Transactional
    void createPersonalizacion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Personalizacion
        var returnedPersonalizacion = om.readValue(
            restPersonalizacionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personalizacion)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Personalizacion.class
        );

        // Validate the Personalizacion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPersonalizacionUpdatableFieldsEquals(returnedPersonalizacion, getPersistedPersonalizacion(returnedPersonalizacion));

        insertedPersonalizacion = returnedPersonalizacion;
    }

    @Test
    @Transactional
    void createPersonalizacionWithExistingId() throws Exception {
        // Create the Personalizacion with an existing ID
        personalizacion.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonalizacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personalizacion)))
            .andExpect(status().isBadRequest());

        // Validate the Personalizacion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPersonalizacions() throws Exception {
        // Initialize the database
        insertedPersonalizacion = personalizacionRepository.saveAndFlush(personalizacion);

        // Get all the personalizacionList
        restPersonalizacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personalizacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getPersonalizacion() throws Exception {
        // Initialize the database
        insertedPersonalizacion = personalizacionRepository.saveAndFlush(personalizacion);

        // Get the personalizacion
        restPersonalizacionMockMvc
            .perform(get(ENTITY_API_URL_ID, personalizacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personalizacion.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingPersonalizacion() throws Exception {
        // Get the personalizacion
        restPersonalizacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPersonalizacion() throws Exception {
        // Initialize the database
        insertedPersonalizacion = personalizacionRepository.saveAndFlush(personalizacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the personalizacion
        Personalizacion updatedPersonalizacion = personalizacionRepository.findById(personalizacion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPersonalizacion are not directly saved in db
        em.detach(updatedPersonalizacion);
        updatedPersonalizacion.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restPersonalizacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonalizacion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPersonalizacion))
            )
            .andExpect(status().isOk());

        // Validate the Personalizacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPersonalizacionToMatchAllProperties(updatedPersonalizacion);
    }

    @Test
    @Transactional
    void putNonExistingPersonalizacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personalizacion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalizacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personalizacion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(personalizacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personalizacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonalizacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personalizacion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalizacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(personalizacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personalizacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonalizacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personalizacion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalizacionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personalizacion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personalizacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonalizacionWithPatch() throws Exception {
        // Initialize the database
        insertedPersonalizacion = personalizacionRepository.saveAndFlush(personalizacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the personalizacion using partial update
        Personalizacion partialUpdatedPersonalizacion = new Personalizacion();
        partialUpdatedPersonalizacion.setId(personalizacion.getId());

        partialUpdatedPersonalizacion.descripcion(UPDATED_DESCRIPCION);

        restPersonalizacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalizacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPersonalizacion))
            )
            .andExpect(status().isOk());

        // Validate the Personalizacion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersonalizacionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPersonalizacion, personalizacion),
            getPersistedPersonalizacion(personalizacion)
        );
    }

    @Test
    @Transactional
    void fullUpdatePersonalizacionWithPatch() throws Exception {
        // Initialize the database
        insertedPersonalizacion = personalizacionRepository.saveAndFlush(personalizacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the personalizacion using partial update
        Personalizacion partialUpdatedPersonalizacion = new Personalizacion();
        partialUpdatedPersonalizacion.setId(personalizacion.getId());

        partialUpdatedPersonalizacion.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restPersonalizacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalizacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPersonalizacion))
            )
            .andExpect(status().isOk());

        // Validate the Personalizacion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersonalizacionUpdatableFieldsEquals(
            partialUpdatedPersonalizacion,
            getPersistedPersonalizacion(partialUpdatedPersonalizacion)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPersonalizacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personalizacion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalizacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personalizacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(personalizacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personalizacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonalizacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personalizacion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalizacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(personalizacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personalizacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonalizacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personalizacion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalizacionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(personalizacion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personalizacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonalizacion() throws Exception {
        // Initialize the database
        insertedPersonalizacion = personalizacionRepository.saveAndFlush(personalizacion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the personalizacion
        restPersonalizacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, personalizacion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return personalizacionRepository.count();
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

    protected Personalizacion getPersistedPersonalizacion(Personalizacion personalizacion) {
        return personalizacionRepository.findById(personalizacion.getId()).orElseThrow();
    }

    protected void assertPersistedPersonalizacionToMatchAllProperties(Personalizacion expectedPersonalizacion) {
        assertPersonalizacionAllPropertiesEquals(expectedPersonalizacion, getPersistedPersonalizacion(expectedPersonalizacion));
    }

    protected void assertPersistedPersonalizacionToMatchUpdatableProperties(Personalizacion expectedPersonalizacion) {
        assertPersonalizacionAllUpdatablePropertiesEquals(expectedPersonalizacion, getPersistedPersonalizacion(expectedPersonalizacion));
    }
}

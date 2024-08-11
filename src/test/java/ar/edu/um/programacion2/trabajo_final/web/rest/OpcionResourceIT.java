package ar.edu.um.programacion2.trabajo_final.web.rest;

import static ar.edu.um.programacion2.trabajo_final.domain.OpcionAsserts.*;
import static ar.edu.um.programacion2.trabajo_final.web.rest.TestUtil.createUpdateProxyForBean;
import static ar.edu.um.programacion2.trabajo_final.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.trabajo_final.IntegrationTest;
import ar.edu.um.programacion2.trabajo_final.domain.Opcion;
import ar.edu.um.programacion2.trabajo_final.repository.OpcionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link OpcionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OpcionResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRECIO_ADICIONAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRECIO_ADICIONAL = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/opcions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OpcionRepository opcionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOpcionMockMvc;

    private Opcion opcion;

    private Opcion insertedOpcion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Opcion createEntity(EntityManager em) {
        Opcion opcion = new Opcion()
            .codigo(DEFAULT_CODIGO)
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .precioAdicional(DEFAULT_PRECIO_ADICIONAL);
        return opcion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Opcion createUpdatedEntity(EntityManager em) {
        Opcion opcion = new Opcion()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioAdicional(UPDATED_PRECIO_ADICIONAL);
        return opcion;
    }

    @BeforeEach
    public void initTest() {
        opcion = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedOpcion != null) {
            opcionRepository.delete(insertedOpcion);
            insertedOpcion = null;
        }
    }

    @Test
    @Transactional
    void createOpcion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Opcion
        var returnedOpcion = om.readValue(
            restOpcionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(opcion)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Opcion.class
        );

        // Validate the Opcion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOpcionUpdatableFieldsEquals(returnedOpcion, getPersistedOpcion(returnedOpcion));

        insertedOpcion = returnedOpcion;
    }

    @Test
    @Transactional
    void createOpcionWithExistingId() throws Exception {
        // Create the Opcion with an existing ID
        opcion.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOpcionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(opcion)))
            .andExpect(status().isBadRequest());

        // Validate the Opcion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOpcions() throws Exception {
        // Initialize the database
        insertedOpcion = opcionRepository.saveAndFlush(opcion);

        // Get all the opcionList
        restOpcionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(opcion.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].precioAdicional").value(hasItem(sameNumber(DEFAULT_PRECIO_ADICIONAL))));
    }

    @Test
    @Transactional
    void getOpcion() throws Exception {
        // Initialize the database
        insertedOpcion = opcionRepository.saveAndFlush(opcion);

        // Get the opcion
        restOpcionMockMvc
            .perform(get(ENTITY_API_URL_ID, opcion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(opcion.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.precioAdicional").value(sameNumber(DEFAULT_PRECIO_ADICIONAL)));
    }

    @Test
    @Transactional
    void getNonExistingOpcion() throws Exception {
        // Get the opcion
        restOpcionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOpcion() throws Exception {
        // Initialize the database
        insertedOpcion = opcionRepository.saveAndFlush(opcion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the opcion
        Opcion updatedOpcion = opcionRepository.findById(opcion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOpcion are not directly saved in db
        em.detach(updatedOpcion);
        updatedOpcion
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioAdicional(UPDATED_PRECIO_ADICIONAL);

        restOpcionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOpcion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOpcion))
            )
            .andExpect(status().isOk());

        // Validate the Opcion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOpcionToMatchAllProperties(updatedOpcion);
    }

    @Test
    @Transactional
    void putNonExistingOpcion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        opcion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpcionMockMvc
            .perform(put(ENTITY_API_URL_ID, opcion.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(opcion)))
            .andExpect(status().isBadRequest());

        // Validate the Opcion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOpcion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        opcion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpcionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(opcion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opcion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOpcion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        opcion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpcionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(opcion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Opcion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOpcionWithPatch() throws Exception {
        // Initialize the database
        insertedOpcion = opcionRepository.saveAndFlush(opcion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the opcion using partial update
        Opcion partialUpdatedOpcion = new Opcion();
        partialUpdatedOpcion.setId(opcion.getId());

        partialUpdatedOpcion.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restOpcionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOpcion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOpcion))
            )
            .andExpect(status().isOk());

        // Validate the Opcion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOpcionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOpcion, opcion), getPersistedOpcion(opcion));
    }

    @Test
    @Transactional
    void fullUpdateOpcionWithPatch() throws Exception {
        // Initialize the database
        insertedOpcion = opcionRepository.saveAndFlush(opcion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the opcion using partial update
        Opcion partialUpdatedOpcion = new Opcion();
        partialUpdatedOpcion.setId(opcion.getId());

        partialUpdatedOpcion
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioAdicional(UPDATED_PRECIO_ADICIONAL);

        restOpcionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOpcion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOpcion))
            )
            .andExpect(status().isOk());

        // Validate the Opcion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOpcionUpdatableFieldsEquals(partialUpdatedOpcion, getPersistedOpcion(partialUpdatedOpcion));
    }

    @Test
    @Transactional
    void patchNonExistingOpcion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        opcion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpcionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, opcion.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(opcion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opcion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOpcion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        opcion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpcionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(opcion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opcion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOpcion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        opcion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpcionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(opcion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Opcion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOpcion() throws Exception {
        // Initialize the database
        insertedOpcion = opcionRepository.saveAndFlush(opcion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the opcion
        restOpcionMockMvc
            .perform(delete(ENTITY_API_URL_ID, opcion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return opcionRepository.count();
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

    protected Opcion getPersistedOpcion(Opcion opcion) {
        return opcionRepository.findById(opcion.getId()).orElseThrow();
    }

    protected void assertPersistedOpcionToMatchAllProperties(Opcion expectedOpcion) {
        assertOpcionAllPropertiesEquals(expectedOpcion, getPersistedOpcion(expectedOpcion));
    }

    protected void assertPersistedOpcionToMatchUpdatableProperties(Opcion expectedOpcion) {
        assertOpcionAllUpdatablePropertiesEquals(expectedOpcion, getPersistedOpcion(expectedOpcion));
    }
}

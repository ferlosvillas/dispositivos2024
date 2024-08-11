package ar.edu.um.programacion2.trabajo_final.web.rest;

import static ar.edu.um.programacion2.trabajo_final.domain.AdicionalAsserts.*;
import static ar.edu.um.programacion2.trabajo_final.web.rest.TestUtil.createUpdateProxyForBean;
import static ar.edu.um.programacion2.trabajo_final.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.trabajo_final.IntegrationTest;
import ar.edu.um.programacion2.trabajo_final.domain.Adicional;
import ar.edu.um.programacion2.trabajo_final.repository.AdicionalRepository;
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
 * Integration tests for the {@link AdicionalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdicionalResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRECIO = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRECIO = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PRECIO_GRATIS = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRECIO_GRATIS = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/adicionals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AdicionalRepository adicionalRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdicionalMockMvc;

    private Adicional adicional;

    private Adicional insertedAdicional;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adicional createEntity(EntityManager em) {
        Adicional adicional = new Adicional()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .precio(DEFAULT_PRECIO)
            .precioGratis(DEFAULT_PRECIO_GRATIS);
        return adicional;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adicional createUpdatedEntity(EntityManager em) {
        Adicional adicional = new Adicional()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precio(UPDATED_PRECIO)
            .precioGratis(UPDATED_PRECIO_GRATIS);
        return adicional;
    }

    @BeforeEach
    public void initTest() {
        adicional = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAdicional != null) {
            adicionalRepository.delete(insertedAdicional);
            insertedAdicional = null;
        }
    }

    @Test
    @Transactional
    void createAdicional() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Adicional
        var returnedAdicional = om.readValue(
            restAdicionalMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adicional)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Adicional.class
        );

        // Validate the Adicional in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAdicionalUpdatableFieldsEquals(returnedAdicional, getPersistedAdicional(returnedAdicional));

        insertedAdicional = returnedAdicional;
    }

    @Test
    @Transactional
    void createAdicionalWithExistingId() throws Exception {
        // Create the Adicional with an existing ID
        adicional.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdicionalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adicional)))
            .andExpect(status().isBadRequest());

        // Validate the Adicional in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAdicionals() throws Exception {
        // Initialize the database
        insertedAdicional = adicionalRepository.saveAndFlush(adicional);

        // Get all the adicionalList
        restAdicionalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adicional.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(sameNumber(DEFAULT_PRECIO))))
            .andExpect(jsonPath("$.[*].precioGratis").value(hasItem(sameNumber(DEFAULT_PRECIO_GRATIS))));
    }

    @Test
    @Transactional
    void getAdicional() throws Exception {
        // Initialize the database
        insertedAdicional = adicionalRepository.saveAndFlush(adicional);

        // Get the adicional
        restAdicionalMockMvc
            .perform(get(ENTITY_API_URL_ID, adicional.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adicional.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.precio").value(sameNumber(DEFAULT_PRECIO)))
            .andExpect(jsonPath("$.precioGratis").value(sameNumber(DEFAULT_PRECIO_GRATIS)));
    }

    @Test
    @Transactional
    void getNonExistingAdicional() throws Exception {
        // Get the adicional
        restAdicionalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdicional() throws Exception {
        // Initialize the database
        insertedAdicional = adicionalRepository.saveAndFlush(adicional);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the adicional
        Adicional updatedAdicional = adicionalRepository.findById(adicional.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAdicional are not directly saved in db
        em.detach(updatedAdicional);
        updatedAdicional.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).precio(UPDATED_PRECIO).precioGratis(UPDATED_PRECIO_GRATIS);

        restAdicionalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAdicional.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAdicional))
            )
            .andExpect(status().isOk());

        // Validate the Adicional in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAdicionalToMatchAllProperties(updatedAdicional);
    }

    @Test
    @Transactional
    void putNonExistingAdicional() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adicional.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdicionalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adicional.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adicional))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adicional in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdicional() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adicional.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdicionalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(adicional))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adicional in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdicional() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adicional.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdicionalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adicional)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Adicional in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdicionalWithPatch() throws Exception {
        // Initialize the database
        insertedAdicional = adicionalRepository.saveAndFlush(adicional);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the adicional using partial update
        Adicional partialUpdatedAdicional = new Adicional();
        partialUpdatedAdicional.setId(adicional.getId());

        partialUpdatedAdicional.nombre(UPDATED_NOMBRE).precio(UPDATED_PRECIO);

        restAdicionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdicional.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdicional))
            )
            .andExpect(status().isOk());

        // Validate the Adicional in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdicionalUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAdicional, adicional),
            getPersistedAdicional(adicional)
        );
    }

    @Test
    @Transactional
    void fullUpdateAdicionalWithPatch() throws Exception {
        // Initialize the database
        insertedAdicional = adicionalRepository.saveAndFlush(adicional);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the adicional using partial update
        Adicional partialUpdatedAdicional = new Adicional();
        partialUpdatedAdicional.setId(adicional.getId());

        partialUpdatedAdicional
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precio(UPDATED_PRECIO)
            .precioGratis(UPDATED_PRECIO_GRATIS);

        restAdicionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdicional.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdicional))
            )
            .andExpect(status().isOk());

        // Validate the Adicional in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdicionalUpdatableFieldsEquals(partialUpdatedAdicional, getPersistedAdicional(partialUpdatedAdicional));
    }

    @Test
    @Transactional
    void patchNonExistingAdicional() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adicional.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdicionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adicional.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(adicional))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adicional in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdicional() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adicional.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdicionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(adicional))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adicional in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdicional() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adicional.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdicionalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(adicional)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Adicional in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdicional() throws Exception {
        // Initialize the database
        insertedAdicional = adicionalRepository.saveAndFlush(adicional);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the adicional
        restAdicionalMockMvc
            .perform(delete(ENTITY_API_URL_ID, adicional.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return adicionalRepository.count();
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

    protected Adicional getPersistedAdicional(Adicional adicional) {
        return adicionalRepository.findById(adicional.getId()).orElseThrow();
    }

    protected void assertPersistedAdicionalToMatchAllProperties(Adicional expectedAdicional) {
        assertAdicionalAllPropertiesEquals(expectedAdicional, getPersistedAdicional(expectedAdicional));
    }

    protected void assertPersistedAdicionalToMatchUpdatableProperties(Adicional expectedAdicional) {
        assertAdicionalAllUpdatablePropertiesEquals(expectedAdicional, getPersistedAdicional(expectedAdicional));
    }
}

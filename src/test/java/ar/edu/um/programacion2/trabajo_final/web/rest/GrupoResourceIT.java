package ar.edu.um.programacion2.trabajo_final.web.rest;

import static ar.edu.um.programacion2.trabajo_final.domain.GrupoAsserts.*;
import static ar.edu.um.programacion2.trabajo_final.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.trabajo_final.IntegrationTest;
import ar.edu.um.programacion2.trabajo_final.domain.Grupo;
import ar.edu.um.programacion2.trabajo_final.repository.GrupoRepository;
import ar.edu.um.programacion2.trabajo_final.repository.UserRepository;
import ar.edu.um.programacion2.trabajo_final.service.GrupoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GrupoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class GrupoResourceIT {

    private static final UUID DEFAULT_ID_GRUPO = UUID.randomUUID();
    private static final UUID UPDATED_ID_GRUPO = UUID.randomUUID();

    private static final String DEFAULT_NOMBRES = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRES = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/grupos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private GrupoRepository grupoRepositoryMock;

    @Mock
    private GrupoService grupoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGrupoMockMvc;

    private Grupo grupo;

    private Grupo insertedGrupo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Grupo createEntity(EntityManager em) {
        Grupo grupo = new Grupo().idGrupo(DEFAULT_ID_GRUPO).nombres(DEFAULT_NOMBRES).descripcion(DEFAULT_DESCRIPCION);
        return grupo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Grupo createUpdatedEntity(EntityManager em) {
        Grupo grupo = new Grupo().idGrupo(UPDATED_ID_GRUPO).nombres(UPDATED_NOMBRES).descripcion(UPDATED_DESCRIPCION);
        return grupo;
    }

    @BeforeEach
    public void initTest() {
        grupo = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedGrupo != null) {
            grupoRepository.delete(insertedGrupo);
            insertedGrupo = null;
        }
    }

    @Test
    @Transactional
    void createGrupo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Grupo
        var returnedGrupo = om.readValue(
            restGrupoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(grupo)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Grupo.class
        );

        // Validate the Grupo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertGrupoUpdatableFieldsEquals(returnedGrupo, getPersistedGrupo(returnedGrupo));

        insertedGrupo = returnedGrupo;
    }

    @Test
    @Transactional
    void createGrupoWithExistingId() throws Exception {
        // Create the Grupo with an existing ID
        grupo.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGrupoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(grupo)))
            .andExpect(status().isBadRequest());

        // Validate the Grupo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGrupos() throws Exception {
        // Initialize the database
        insertedGrupo = grupoRepository.saveAndFlush(grupo);

        // Get all the grupoList
        restGrupoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(grupo.getId().intValue())))
            .andExpect(jsonPath("$.[*].idGrupo").value(hasItem(DEFAULT_ID_GRUPO.toString())))
            .andExpect(jsonPath("$.[*].nombres").value(hasItem(DEFAULT_NOMBRES)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGruposWithEagerRelationshipsIsEnabled() throws Exception {
        when(grupoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGrupoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(grupoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGruposWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(grupoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGrupoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(grupoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getGrupo() throws Exception {
        // Initialize the database
        insertedGrupo = grupoRepository.saveAndFlush(grupo);

        // Get the grupo
        restGrupoMockMvc
            .perform(get(ENTITY_API_URL_ID, grupo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(grupo.getId().intValue()))
            .andExpect(jsonPath("$.idGrupo").value(DEFAULT_ID_GRUPO.toString()))
            .andExpect(jsonPath("$.nombres").value(DEFAULT_NOMBRES))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingGrupo() throws Exception {
        // Get the grupo
        restGrupoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGrupo() throws Exception {
        // Initialize the database
        insertedGrupo = grupoRepository.saveAndFlush(grupo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the grupo
        Grupo updatedGrupo = grupoRepository.findById(grupo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGrupo are not directly saved in db
        em.detach(updatedGrupo);
        updatedGrupo.idGrupo(UPDATED_ID_GRUPO).nombres(UPDATED_NOMBRES).descripcion(UPDATED_DESCRIPCION);

        restGrupoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGrupo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedGrupo))
            )
            .andExpect(status().isOk());

        // Validate the Grupo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGrupoToMatchAllProperties(updatedGrupo);
    }

    @Test
    @Transactional
    void putNonExistingGrupo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grupo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGrupoMockMvc
            .perform(put(ENTITY_API_URL_ID, grupo.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(grupo)))
            .andExpect(status().isBadRequest());

        // Validate the Grupo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGrupo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grupo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(grupo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Grupo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGrupo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grupo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(grupo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Grupo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGrupoWithPatch() throws Exception {
        // Initialize the database
        insertedGrupo = grupoRepository.saveAndFlush(grupo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the grupo using partial update
        Grupo partialUpdatedGrupo = new Grupo();
        partialUpdatedGrupo.setId(grupo.getId());

        partialUpdatedGrupo.nombres(UPDATED_NOMBRES);

        restGrupoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGrupo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGrupo))
            )
            .andExpect(status().isOk());

        // Validate the Grupo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGrupoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedGrupo, grupo), getPersistedGrupo(grupo));
    }

    @Test
    @Transactional
    void fullUpdateGrupoWithPatch() throws Exception {
        // Initialize the database
        insertedGrupo = grupoRepository.saveAndFlush(grupo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the grupo using partial update
        Grupo partialUpdatedGrupo = new Grupo();
        partialUpdatedGrupo.setId(grupo.getId());

        partialUpdatedGrupo.idGrupo(UPDATED_ID_GRUPO).nombres(UPDATED_NOMBRES).descripcion(UPDATED_DESCRIPCION);

        restGrupoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGrupo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGrupo))
            )
            .andExpect(status().isOk());

        // Validate the Grupo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGrupoUpdatableFieldsEquals(partialUpdatedGrupo, getPersistedGrupo(partialUpdatedGrupo));
    }

    @Test
    @Transactional
    void patchNonExistingGrupo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grupo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGrupoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, grupo.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(grupo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Grupo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGrupo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grupo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(grupo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Grupo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGrupo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grupo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(grupo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Grupo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGrupo() throws Exception {
        // Initialize the database
        insertedGrupo = grupoRepository.saveAndFlush(grupo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the grupo
        restGrupoMockMvc
            .perform(delete(ENTITY_API_URL_ID, grupo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return grupoRepository.count();
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

    protected Grupo getPersistedGrupo(Grupo grupo) {
        return grupoRepository.findById(grupo.getId()).orElseThrow();
    }

    protected void assertPersistedGrupoToMatchAllProperties(Grupo expectedGrupo) {
        assertGrupoAllPropertiesEquals(expectedGrupo, getPersistedGrupo(expectedGrupo));
    }

    protected void assertPersistedGrupoToMatchUpdatableProperties(Grupo expectedGrupo) {
        assertGrupoAllUpdatablePropertiesEquals(expectedGrupo, getPersistedGrupo(expectedGrupo));
    }
}

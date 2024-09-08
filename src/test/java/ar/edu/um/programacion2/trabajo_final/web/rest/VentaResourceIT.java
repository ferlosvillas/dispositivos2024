package ar.edu.um.programacion2.trabajo_final.web.rest;

import static ar.edu.um.programacion2.trabajo_final.domain.VentaAsserts.*;
import static ar.edu.um.programacion2.trabajo_final.web.rest.TestUtil.createUpdateProxyForBean;
import static ar.edu.um.programacion2.trabajo_final.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.trabajo_final.IntegrationTest;
import ar.edu.um.programacion2.trabajo_final.domain.Venta;
import ar.edu.um.programacion2.trabajo_final.repository.VentaRepository;
import ar.edu.um.programacion2.trabajo_final.service.VentaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;
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
 * Integration tests for the {@link VentaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VentaResourceIT {

    private static final Long DEFAULT_ID_VENTA = 1L;
    private static final Long UPDATED_ID_VENTA = 2L;

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRECIO = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRECIO = new BigDecimal(2);

    private static final String DEFAULT_VENTA_PEDIDO_JSON = "AAAAAAAAAA";
    private static final String UPDATED_VENTA_PEDIDO_JSON = "BBBBBBBBBB";

    private static final String DEFAULT_VENTA_RESULTADO_JSON = "AAAAAAAAAA";
    private static final String UPDATED_VENTA_RESULTADO_JSON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ventas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VentaRepository ventaRepository;

    @Mock
    private VentaRepository ventaRepositoryMock;

    @Mock
    private VentaService ventaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVentaMockMvc;

    private Venta venta;

    private Venta insertedVenta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Venta createEntity(EntityManager em) {
        Venta venta = new Venta()
            .idVenta(DEFAULT_ID_VENTA)
            .codigo(DEFAULT_CODIGO)
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .precio(DEFAULT_PRECIO)
            .ventaPedidoJson(DEFAULT_VENTA_PEDIDO_JSON)
            .ventaResultadoJson(DEFAULT_VENTA_RESULTADO_JSON);
        return venta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Venta createUpdatedEntity(EntityManager em) {
        Venta venta = new Venta()
            .idVenta(UPDATED_ID_VENTA)
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precio(UPDATED_PRECIO)
            .ventaPedidoJson(UPDATED_VENTA_PEDIDO_JSON)
            .ventaResultadoJson(UPDATED_VENTA_RESULTADO_JSON);
        return venta;
    }

    @BeforeEach
    public void initTest() {
        venta = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedVenta != null) {
            ventaRepository.delete(insertedVenta);
            insertedVenta = null;
        }
    }

    @Test
    @Transactional
    void createVenta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Venta
        var returnedVenta = om.readValue(
            restVentaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(venta)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Venta.class
        );

        // Validate the Venta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVentaUpdatableFieldsEquals(returnedVenta, getPersistedVenta(returnedVenta));

        insertedVenta = returnedVenta;
    }

    @Test
    @Transactional
    void createVentaWithExistingId() throws Exception {
        // Create the Venta with an existing ID
        venta.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(venta)))
            .andExpect(status().isBadRequest());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVentas() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        // Get all the ventaList
        restVentaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(venta.getId().intValue())))
            .andExpect(jsonPath("$.[*].idVenta").value(hasItem(DEFAULT_ID_VENTA.intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(sameNumber(DEFAULT_PRECIO))))
            .andExpect(jsonPath("$.[*].ventaPedidoJson").value(hasItem(DEFAULT_VENTA_PEDIDO_JSON.toString())))
            .andExpect(jsonPath("$.[*].ventaResultadoJson").value(hasItem(DEFAULT_VENTA_RESULTADO_JSON.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVentasWithEagerRelationshipsIsEnabled() throws Exception {
        when(ventaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVentaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ventaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVentasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ventaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVentaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(ventaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getVenta() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        // Get the venta
        restVentaMockMvc
            .perform(get(ENTITY_API_URL_ID, venta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(venta.getId().intValue()))
            .andExpect(jsonPath("$.idVenta").value(DEFAULT_ID_VENTA.intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.precio").value(sameNumber(DEFAULT_PRECIO)))
            .andExpect(jsonPath("$.ventaPedidoJson").value(DEFAULT_VENTA_PEDIDO_JSON.toString()))
            .andExpect(jsonPath("$.ventaResultadoJson").value(DEFAULT_VENTA_RESULTADO_JSON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVenta() throws Exception {
        // Get the venta
        restVentaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVenta() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the venta
        Venta updatedVenta = ventaRepository.findById(venta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVenta are not directly saved in db
        em.detach(updatedVenta);
        updatedVenta
            .idVenta(UPDATED_ID_VENTA)
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precio(UPDATED_PRECIO)
            .ventaPedidoJson(UPDATED_VENTA_PEDIDO_JSON)
            .ventaResultadoJson(UPDATED_VENTA_RESULTADO_JSON);

        restVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVenta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedVenta))
            )
            .andExpect(status().isOk());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVentaToMatchAllProperties(updatedVenta);
    }

    @Test
    @Transactional
    void putNonExistingVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(put(ENTITY_API_URL_ID, venta.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(venta)))
            .andExpect(status().isBadRequest());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(venta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(venta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVentaWithPatch() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the venta using partial update
        Venta partialUpdatedVenta = new Venta();
        partialUpdatedVenta.setId(venta.getId());

        partialUpdatedVenta.idVenta(UPDATED_ID_VENTA).codigo(UPDATED_CODIGO).ventaPedidoJson(UPDATED_VENTA_PEDIDO_JSON);

        restVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVenta))
            )
            .andExpect(status().isOk());

        // Validate the Venta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVentaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVenta, venta), getPersistedVenta(venta));
    }

    @Test
    @Transactional
    void fullUpdateVentaWithPatch() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the venta using partial update
        Venta partialUpdatedVenta = new Venta();
        partialUpdatedVenta.setId(venta.getId());

        partialUpdatedVenta
            .idVenta(UPDATED_ID_VENTA)
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precio(UPDATED_PRECIO)
            .ventaPedidoJson(UPDATED_VENTA_PEDIDO_JSON)
            .ventaResultadoJson(UPDATED_VENTA_RESULTADO_JSON);

        restVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVenta))
            )
            .andExpect(status().isOk());

        // Validate the Venta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVentaUpdatableFieldsEquals(partialUpdatedVenta, getPersistedVenta(partialUpdatedVenta));
    }

    @Test
    @Transactional
    void patchNonExistingVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, venta.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(venta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(venta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(venta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVenta() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the venta
        restVentaMockMvc
            .perform(delete(ENTITY_API_URL_ID, venta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ventaRepository.count();
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

    protected Venta getPersistedVenta(Venta venta) {
        return ventaRepository.findById(venta.getId()).orElseThrow();
    }

    protected void assertPersistedVentaToMatchAllProperties(Venta expectedVenta) {
        assertVentaAllPropertiesEquals(expectedVenta, getPersistedVenta(expectedVenta));
    }

    protected void assertPersistedVentaToMatchUpdatableProperties(Venta expectedVenta) {
        assertVentaAllUpdatablePropertiesEquals(expectedVenta, getPersistedVenta(expectedVenta));
    }
}

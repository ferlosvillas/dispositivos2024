package ar.edu.um.programacion2.trabajo_final.service;

import ar.edu.um.programacion2.trabajo_final.domain.Venta;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ar.edu.um.programacion2.trabajo_final.domain.Venta}.
 */
public interface VentaService {
    /**
     * Save a venta.
     *
     * @param venta the entity to save.
     * @return the persisted entity.
     */
    Venta save(Venta venta);

    /**
     * Updates a venta.
     *
     * @param venta the entity to update.
     * @return the persisted entity.
     */
    Venta update(Venta venta);

    /**
     * Partially updates a venta.
     *
     * @param venta the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Venta> partialUpdate(Venta venta);

    /**
     * Get all the ventas.
     *
     * @return the list of entities.
     */
    List<Venta> findAll();

    /**
     * Get all the ventas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Venta> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" venta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Venta> findOne(Long id);

    /**
     * Delete the "id" venta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

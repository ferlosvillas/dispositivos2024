package ar.edu.um.programacion2.trabajo_final.service;

import ar.edu.um.programacion2.trabajo_final.domain.Opcion;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ar.edu.um.programacion2.trabajo_final.domain.Opcion}.
 */
public interface OpcionService {
    /**
     * Save a opcion.
     *
     * @param opcion the entity to save.
     * @return the persisted entity.
     */
    Opcion save(Opcion opcion);

    /**
     * Updates a opcion.
     *
     * @param opcion the entity to update.
     * @return the persisted entity.
     */
    Opcion update(Opcion opcion);

    /**
     * Partially updates a opcion.
     *
     * @param opcion the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Opcion> partialUpdate(Opcion opcion);

    /**
     * Get all the opcions.
     *
     * @return the list of entities.
     */
    List<Opcion> findAll();

    /**
     * Get the "id" opcion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Opcion> findOne(Long id);

    /**
     * Delete the "id" opcion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

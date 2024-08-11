package ar.edu.um.programacion2.trabajo_final.service;

import ar.edu.um.programacion2.trabajo_final.domain.Caracteristica;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ar.edu.um.programacion2.trabajo_final.domain.Caracteristica}.
 */
public interface CaracteristicaService {
    /**
     * Save a caracteristica.
     *
     * @param caracteristica the entity to save.
     * @return the persisted entity.
     */
    Caracteristica save(Caracteristica caracteristica);

    /**
     * Updates a caracteristica.
     *
     * @param caracteristica the entity to update.
     * @return the persisted entity.
     */
    Caracteristica update(Caracteristica caracteristica);

    /**
     * Partially updates a caracteristica.
     *
     * @param caracteristica the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Caracteristica> partialUpdate(Caracteristica caracteristica);

    /**
     * Get all the caracteristicas.
     *
     * @return the list of entities.
     */
    List<Caracteristica> findAll();

    /**
     * Get the "id" caracteristica.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Caracteristica> findOne(Long id);

    /**
     * Delete the "id" caracteristica.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

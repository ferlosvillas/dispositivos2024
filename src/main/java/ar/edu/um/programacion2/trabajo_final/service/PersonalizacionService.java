package ar.edu.um.programacion2.trabajo_final.service;

import ar.edu.um.programacion2.trabajo_final.domain.Personalizacion;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ar.edu.um.programacion2.trabajo_final.domain.Personalizacion}.
 */
public interface PersonalizacionService {
    /**
     * Save a personalizacion.
     *
     * @param personalizacion the entity to save.
     * @return the persisted entity.
     */
    Personalizacion save(Personalizacion personalizacion);

    /**
     * Updates a personalizacion.
     *
     * @param personalizacion the entity to update.
     * @return the persisted entity.
     */
    Personalizacion update(Personalizacion personalizacion);

    /**
     * Partially updates a personalizacion.
     *
     * @param personalizacion the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Personalizacion> partialUpdate(Personalizacion personalizacion);

    /**
     * Get all the personalizacions.
     *
     * @return the list of entities.
     */
    List<Personalizacion> findAll();

    /**
     * Get the "id" personalizacion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Personalizacion> findOne(Long id);

    /**
     * Delete the "id" personalizacion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

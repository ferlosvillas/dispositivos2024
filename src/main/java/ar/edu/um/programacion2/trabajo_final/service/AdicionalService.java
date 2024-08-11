package ar.edu.um.programacion2.trabajo_final.service;

import ar.edu.um.programacion2.trabajo_final.domain.Adicional;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ar.edu.um.programacion2.trabajo_final.domain.Adicional}.
 */
public interface AdicionalService {
    /**
     * Save a adicional.
     *
     * @param adicional the entity to save.
     * @return the persisted entity.
     */
    Adicional save(Adicional adicional);

    /**
     * Updates a adicional.
     *
     * @param adicional the entity to update.
     * @return the persisted entity.
     */
    Adicional update(Adicional adicional);

    /**
     * Partially updates a adicional.
     *
     * @param adicional the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Adicional> partialUpdate(Adicional adicional);

    /**
     * Get all the adicionals.
     *
     * @return the list of entities.
     */
    List<Adicional> findAll();

    /**
     * Get the "id" adicional.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Adicional> findOne(Long id);

    /**
     * Delete the "id" adicional.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

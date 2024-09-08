package ar.edu.um.programacion2.trabajo_final.service;

import ar.edu.um.programacion2.trabajo_final.domain.Grupo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ar.edu.um.programacion2.trabajo_final.domain.Grupo}.
 */
public interface GrupoService {
    /**
     * Save a grupo.
     *
     * @param grupo the entity to save.
     * @return the persisted entity.
     */
    Grupo save(Grupo grupo);

    /**
     * Updates a grupo.
     *
     * @param grupo the entity to update.
     * @return the persisted entity.
     */
    Grupo update(Grupo grupo);

    /**
     * Partially updates a grupo.
     *
     * @param grupo the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Grupo> partialUpdate(Grupo grupo);

    /**
     * Get all the grupos.
     *
     * @return the list of entities.
     */
    List<Grupo> findAll();

    /**
     * Get all the grupos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Grupo> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" grupo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Grupo> findOne(Long id);

    /**
     * Delete the "id" grupo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

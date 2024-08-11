package ar.edu.um.programacion2.trabajo_final.service;

import ar.edu.um.programacion2.trabajo_final.domain.Dispositivo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ar.edu.um.programacion2.trabajo_final.domain.Dispositivo}.
 */
public interface DispositivoService {
    /**
     * Save a dispositivo.
     *
     * @param dispositivo the entity to save.
     * @return the persisted entity.
     */
    Dispositivo save(Dispositivo dispositivo);

    /**
     * Updates a dispositivo.
     *
     * @param dispositivo the entity to update.
     * @return the persisted entity.
     */
    Dispositivo update(Dispositivo dispositivo);

    /**
     * Partially updates a dispositivo.
     *
     * @param dispositivo the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Dispositivo> partialUpdate(Dispositivo dispositivo);

    /**
     * Get all the dispositivos.
     *
     * @return the list of entities.
     */
    List<Dispositivo> findAll();

    /**
     * Get all the dispositivos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Dispositivo> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" dispositivo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Dispositivo> findOne(Long id);

    /**
     * Delete the "id" dispositivo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

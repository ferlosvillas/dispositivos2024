package ar.edu.um.programacion2.trabajo_final.web.rest;

import ar.edu.um.programacion2.trabajo_final.domain.Grupo;
import ar.edu.um.programacion2.trabajo_final.repository.GrupoRepository;
import ar.edu.um.programacion2.trabajo_final.service.GrupoService;
import ar.edu.um.programacion2.trabajo_final.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.programacion2.trabajo_final.domain.Grupo}.
 */
@RestController
@RequestMapping("/api/grupos")
public class GrupoResource {

    private static final Logger log = LoggerFactory.getLogger(GrupoResource.class);

    private static final String ENTITY_NAME = "grupo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GrupoService grupoService;

    private final GrupoRepository grupoRepository;

    public GrupoResource(GrupoService grupoService, GrupoRepository grupoRepository) {
        this.grupoService = grupoService;
        this.grupoRepository = grupoRepository;
    }

    /**
     * {@code POST  /grupos} : Create a new grupo.
     *
     * @param grupo the grupo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new grupo, or with status {@code 400 (Bad Request)} if the grupo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Grupo> createGrupo(@RequestBody Grupo grupo) throws URISyntaxException {
        log.debug("REST request to save Grupo : {}", grupo);
        if (grupo.getId() != null) {
            throw new BadRequestAlertException("A new grupo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        grupo = grupoService.save(grupo);
        return ResponseEntity.created(new URI("/api/grupos/" + grupo.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, grupo.getId().toString()))
            .body(grupo);
    }

    /**
     * {@code PUT  /grupos/:id} : Updates an existing grupo.
     *
     * @param id the id of the grupo to save.
     * @param grupo the grupo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated grupo,
     * or with status {@code 400 (Bad Request)} if the grupo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the grupo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Grupo> updateGrupo(@PathVariable(value = "id", required = false) final Long id, @RequestBody Grupo grupo)
        throws URISyntaxException {
        log.debug("REST request to update Grupo : {}, {}", id, grupo);
        if (grupo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, grupo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!grupoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        grupo = grupoService.update(grupo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, grupo.getId().toString()))
            .body(grupo);
    }

    /**
     * {@code PATCH  /grupos/:id} : Partial updates given fields of an existing grupo, field will ignore if it is null
     *
     * @param id the id of the grupo to save.
     * @param grupo the grupo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated grupo,
     * or with status {@code 400 (Bad Request)} if the grupo is not valid,
     * or with status {@code 404 (Not Found)} if the grupo is not found,
     * or with status {@code 500 (Internal Server Error)} if the grupo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Grupo> partialUpdateGrupo(@PathVariable(value = "id", required = false) final Long id, @RequestBody Grupo grupo)
        throws URISyntaxException {
        log.debug("REST request to partial update Grupo partially : {}, {}", id, grupo);
        if (grupo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, grupo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!grupoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Grupo> result = grupoService.partialUpdate(grupo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, grupo.getId().toString())
        );
    }

    /**
     * {@code GET  /grupos} : get all the grupos.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of grupos in body.
     */
    @GetMapping("")
    public List<Grupo> getAllGrupos(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Grupos");
        return grupoService.findAll();
    }

    /**
     * {@code GET  /grupos/:id} : get the "id" grupo.
     *
     * @param id the id of the grupo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the grupo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Grupo> getGrupo(@PathVariable("id") Long id) {
        log.debug("REST request to get Grupo : {}", id);
        Optional<Grupo> grupo = grupoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(grupo);
    }

    /**
     * {@code DELETE  /grupos/:id} : delete the "id" grupo.
     *
     * @param id the id of the grupo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrupo(@PathVariable("id") Long id) {
        log.debug("REST request to delete Grupo : {}", id);
        grupoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

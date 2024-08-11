package ar.edu.um.programacion2.trabajo_final.web.rest;

import ar.edu.um.programacion2.trabajo_final.domain.Dispositivo;
import ar.edu.um.programacion2.trabajo_final.repository.DispositivoRepository;
import ar.edu.um.programacion2.trabajo_final.service.DispositivoService;
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
 * REST controller for managing {@link ar.edu.um.programacion2.trabajo_final.domain.Dispositivo}.
 */
@RestController
@RequestMapping("/api/dispositivos")
public class DispositivoResource {

    private static final Logger log = LoggerFactory.getLogger(DispositivoResource.class);

    private static final String ENTITY_NAME = "dispositivo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DispositivoService dispositivoService;

    private final DispositivoRepository dispositivoRepository;

    public DispositivoResource(DispositivoService dispositivoService, DispositivoRepository dispositivoRepository) {
        this.dispositivoService = dispositivoService;
        this.dispositivoRepository = dispositivoRepository;
    }

    /**
     * {@code POST  /dispositivos} : Create a new dispositivo.
     *
     * @param dispositivo the dispositivo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dispositivo, or with status {@code 400 (Bad Request)} if the dispositivo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Dispositivo> createDispositivo(@RequestBody Dispositivo dispositivo) throws URISyntaxException {
        log.debug("REST request to save Dispositivo : {}", dispositivo);
        if (dispositivo.getId() != null) {
            throw new BadRequestAlertException("A new dispositivo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dispositivo = dispositivoService.save(dispositivo);
        return ResponseEntity.created(new URI("/api/dispositivos/" + dispositivo.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, dispositivo.getId().toString()))
            .body(dispositivo);
    }

    /**
     * {@code PUT  /dispositivos/:id} : Updates an existing dispositivo.
     *
     * @param id the id of the dispositivo to save.
     * @param dispositivo the dispositivo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dispositivo,
     * or with status {@code 400 (Bad Request)} if the dispositivo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dispositivo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Dispositivo> updateDispositivo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Dispositivo dispositivo
    ) throws URISyntaxException {
        log.debug("REST request to update Dispositivo : {}, {}", id, dispositivo);
        if (dispositivo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dispositivo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dispositivoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        dispositivo = dispositivoService.update(dispositivo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dispositivo.getId().toString()))
            .body(dispositivo);
    }

    /**
     * {@code PATCH  /dispositivos/:id} : Partial updates given fields of an existing dispositivo, field will ignore if it is null
     *
     * @param id the id of the dispositivo to save.
     * @param dispositivo the dispositivo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dispositivo,
     * or with status {@code 400 (Bad Request)} if the dispositivo is not valid,
     * or with status {@code 404 (Not Found)} if the dispositivo is not found,
     * or with status {@code 500 (Internal Server Error)} if the dispositivo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Dispositivo> partialUpdateDispositivo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Dispositivo dispositivo
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dispositivo partially : {}, {}", id, dispositivo);
        if (dispositivo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dispositivo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dispositivoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Dispositivo> result = dispositivoService.partialUpdate(dispositivo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dispositivo.getId().toString())
        );
    }

    /**
     * {@code GET  /dispositivos} : get all the dispositivos.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dispositivos in body.
     */
    @GetMapping("")
    public List<Dispositivo> getAllDispositivos(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all Dispositivos");
        return dispositivoService.findAll();
    }

    /**
     * {@code GET  /dispositivos/:id} : get the "id" dispositivo.
     *
     * @param id the id of the dispositivo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dispositivo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Dispositivo> getDispositivo(@PathVariable("id") Long id) {
        log.debug("REST request to get Dispositivo : {}", id);
        Optional<Dispositivo> dispositivo = dispositivoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dispositivo);
    }

    /**
     * {@code DELETE  /dispositivos/:id} : delete the "id" dispositivo.
     *
     * @param id the id of the dispositivo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDispositivo(@PathVariable("id") Long id) {
        log.debug("REST request to delete Dispositivo : {}", id);
        dispositivoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

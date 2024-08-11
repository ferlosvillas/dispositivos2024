package ar.edu.um.programacion2.trabajo_final.web.rest;

import ar.edu.um.programacion2.trabajo_final.domain.Adicional;
import ar.edu.um.programacion2.trabajo_final.repository.AdicionalRepository;
import ar.edu.um.programacion2.trabajo_final.service.AdicionalService;
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
 * REST controller for managing {@link ar.edu.um.programacion2.trabajo_final.domain.Adicional}.
 */
@RestController
@RequestMapping("/api/adicionals")
public class AdicionalResource {

    private static final Logger log = LoggerFactory.getLogger(AdicionalResource.class);

    private static final String ENTITY_NAME = "adicional";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdicionalService adicionalService;

    private final AdicionalRepository adicionalRepository;

    public AdicionalResource(AdicionalService adicionalService, AdicionalRepository adicionalRepository) {
        this.adicionalService = adicionalService;
        this.adicionalRepository = adicionalRepository;
    }

    /**
     * {@code POST  /adicionals} : Create a new adicional.
     *
     * @param adicional the adicional to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adicional, or with status {@code 400 (Bad Request)} if the adicional has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Adicional> createAdicional(@RequestBody Adicional adicional) throws URISyntaxException {
        log.debug("REST request to save Adicional : {}", adicional);
        if (adicional.getId() != null) {
            throw new BadRequestAlertException("A new adicional cannot already have an ID", ENTITY_NAME, "idexists");
        }
        adicional = adicionalService.save(adicional);
        return ResponseEntity.created(new URI("/api/adicionals/" + adicional.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, adicional.getId().toString()))
            .body(adicional);
    }

    /**
     * {@code PUT  /adicionals/:id} : Updates an existing adicional.
     *
     * @param id the id of the adicional to save.
     * @param adicional the adicional to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adicional,
     * or with status {@code 400 (Bad Request)} if the adicional is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adicional couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Adicional> updateAdicional(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Adicional adicional
    ) throws URISyntaxException {
        log.debug("REST request to update Adicional : {}, {}", id, adicional);
        if (adicional.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adicional.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adicionalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        adicional = adicionalService.update(adicional);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adicional.getId().toString()))
            .body(adicional);
    }

    /**
     * {@code PATCH  /adicionals/:id} : Partial updates given fields of an existing adicional, field will ignore if it is null
     *
     * @param id the id of the adicional to save.
     * @param adicional the adicional to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adicional,
     * or with status {@code 400 (Bad Request)} if the adicional is not valid,
     * or with status {@code 404 (Not Found)} if the adicional is not found,
     * or with status {@code 500 (Internal Server Error)} if the adicional couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Adicional> partialUpdateAdicional(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Adicional adicional
    ) throws URISyntaxException {
        log.debug("REST request to partial update Adicional partially : {}, {}", id, adicional);
        if (adicional.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adicional.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adicionalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Adicional> result = adicionalService.partialUpdate(adicional);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adicional.getId().toString())
        );
    }

    /**
     * {@code GET  /adicionals} : get all the adicionals.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adicionals in body.
     */
    @GetMapping("")
    public List<Adicional> getAllAdicionals() {
        log.debug("REST request to get all Adicionals");
        return adicionalService.findAll();
    }

    /**
     * {@code GET  /adicionals/:id} : get the "id" adicional.
     *
     * @param id the id of the adicional to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adicional, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Adicional> getAdicional(@PathVariable("id") Long id) {
        log.debug("REST request to get Adicional : {}", id);
        Optional<Adicional> adicional = adicionalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adicional);
    }

    /**
     * {@code DELETE  /adicionals/:id} : delete the "id" adicional.
     *
     * @param id the id of the adicional to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdicional(@PathVariable("id") Long id) {
        log.debug("REST request to delete Adicional : {}", id);
        adicionalService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

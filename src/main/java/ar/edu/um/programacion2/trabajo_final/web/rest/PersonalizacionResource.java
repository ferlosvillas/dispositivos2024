package ar.edu.um.programacion2.trabajo_final.web.rest;

import ar.edu.um.programacion2.trabajo_final.domain.Personalizacion;
import ar.edu.um.programacion2.trabajo_final.repository.PersonalizacionRepository;
import ar.edu.um.programacion2.trabajo_final.service.PersonalizacionService;
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
 * REST controller for managing {@link ar.edu.um.programacion2.trabajo_final.domain.Personalizacion}.
 */
@RestController
@RequestMapping("/api/personalizacions")
public class PersonalizacionResource {

    private static final Logger log = LoggerFactory.getLogger(PersonalizacionResource.class);

    private static final String ENTITY_NAME = "personalizacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonalizacionService personalizacionService;

    private final PersonalizacionRepository personalizacionRepository;

    public PersonalizacionResource(PersonalizacionService personalizacionService, PersonalizacionRepository personalizacionRepository) {
        this.personalizacionService = personalizacionService;
        this.personalizacionRepository = personalizacionRepository;
    }

    /**
     * {@code POST  /personalizacions} : Create a new personalizacion.
     *
     * @param personalizacion the personalizacion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personalizacion, or with status {@code 400 (Bad Request)} if the personalizacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Personalizacion> createPersonalizacion(@RequestBody Personalizacion personalizacion) throws URISyntaxException {
        log.debug("REST request to save Personalizacion : {}", personalizacion);
        if (personalizacion.getId() != null) {
            throw new BadRequestAlertException("A new personalizacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        personalizacion = personalizacionService.save(personalizacion);
        return ResponseEntity.created(new URI("/api/personalizacions/" + personalizacion.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, personalizacion.getId().toString()))
            .body(personalizacion);
    }

    /**
     * {@code PUT  /personalizacions/:id} : Updates an existing personalizacion.
     *
     * @param id the id of the personalizacion to save.
     * @param personalizacion the personalizacion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personalizacion,
     * or with status {@code 400 (Bad Request)} if the personalizacion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personalizacion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Personalizacion> updatePersonalizacion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Personalizacion personalizacion
    ) throws URISyntaxException {
        log.debug("REST request to update Personalizacion : {}, {}", id, personalizacion);
        if (personalizacion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personalizacion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personalizacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        personalizacion = personalizacionService.update(personalizacion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personalizacion.getId().toString()))
            .body(personalizacion);
    }

    /**
     * {@code PATCH  /personalizacions/:id} : Partial updates given fields of an existing personalizacion, field will ignore if it is null
     *
     * @param id the id of the personalizacion to save.
     * @param personalizacion the personalizacion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personalizacion,
     * or with status {@code 400 (Bad Request)} if the personalizacion is not valid,
     * or with status {@code 404 (Not Found)} if the personalizacion is not found,
     * or with status {@code 500 (Internal Server Error)} if the personalizacion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Personalizacion> partialUpdatePersonalizacion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Personalizacion personalizacion
    ) throws URISyntaxException {
        log.debug("REST request to partial update Personalizacion partially : {}, {}", id, personalizacion);
        if (personalizacion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personalizacion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personalizacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Personalizacion> result = personalizacionService.partialUpdate(personalizacion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personalizacion.getId().toString())
        );
    }

    /**
     * {@code GET  /personalizacions} : get all the personalizacions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personalizacions in body.
     */
    @GetMapping("")
    public List<Personalizacion> getAllPersonalizacions() {
        log.debug("REST request to get all Personalizacions");
        return personalizacionService.findAll();
    }

    /**
     * {@code GET  /personalizacions/:id} : get the "id" personalizacion.
     *
     * @param id the id of the personalizacion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personalizacion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Personalizacion> getPersonalizacion(@PathVariable("id") Long id) {
        log.debug("REST request to get Personalizacion : {}", id);
        Optional<Personalizacion> personalizacion = personalizacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personalizacion);
    }

    /**
     * {@code DELETE  /personalizacions/:id} : delete the "id" personalizacion.
     *
     * @param id the id of the personalizacion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonalizacion(@PathVariable("id") Long id) {
        log.debug("REST request to delete Personalizacion : {}", id);
        personalizacionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

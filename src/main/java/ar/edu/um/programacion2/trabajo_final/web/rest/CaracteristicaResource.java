package ar.edu.um.programacion2.trabajo_final.web.rest;

import ar.edu.um.programacion2.trabajo_final.domain.Caracteristica;
import ar.edu.um.programacion2.trabajo_final.repository.CaracteristicaRepository;
import ar.edu.um.programacion2.trabajo_final.service.CaracteristicaService;
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
 * REST controller for managing {@link ar.edu.um.programacion2.trabajo_final.domain.Caracteristica}.
 */
@RestController
@RequestMapping("/api/caracteristicas")
public class CaracteristicaResource {

    private static final Logger log = LoggerFactory.getLogger(CaracteristicaResource.class);

    private static final String ENTITY_NAME = "caracteristica";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CaracteristicaService caracteristicaService;

    private final CaracteristicaRepository caracteristicaRepository;

    public CaracteristicaResource(CaracteristicaService caracteristicaService, CaracteristicaRepository caracteristicaRepository) {
        this.caracteristicaService = caracteristicaService;
        this.caracteristicaRepository = caracteristicaRepository;
    }

    /**
     * {@code POST  /caracteristicas} : Create a new caracteristica.
     *
     * @param caracteristica the caracteristica to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new caracteristica, or with status {@code 400 (Bad Request)} if the caracteristica has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Caracteristica> createCaracteristica(@RequestBody Caracteristica caracteristica) throws URISyntaxException {
        log.debug("REST request to save Caracteristica : {}", caracteristica);
        if (caracteristica.getId() != null) {
            throw new BadRequestAlertException("A new caracteristica cannot already have an ID", ENTITY_NAME, "idexists");
        }
        caracteristica = caracteristicaService.save(caracteristica);
        return ResponseEntity.created(new URI("/api/caracteristicas/" + caracteristica.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, caracteristica.getId().toString()))
            .body(caracteristica);
    }

    /**
     * {@code PUT  /caracteristicas/:id} : Updates an existing caracteristica.
     *
     * @param id the id of the caracteristica to save.
     * @param caracteristica the caracteristica to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caracteristica,
     * or with status {@code 400 (Bad Request)} if the caracteristica is not valid,
     * or with status {@code 500 (Internal Server Error)} if the caracteristica couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Caracteristica> updateCaracteristica(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Caracteristica caracteristica
    ) throws URISyntaxException {
        log.debug("REST request to update Caracteristica : {}, {}", id, caracteristica);
        if (caracteristica.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, caracteristica.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!caracteristicaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        caracteristica = caracteristicaService.update(caracteristica);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, caracteristica.getId().toString()))
            .body(caracteristica);
    }

    /**
     * {@code PATCH  /caracteristicas/:id} : Partial updates given fields of an existing caracteristica, field will ignore if it is null
     *
     * @param id the id of the caracteristica to save.
     * @param caracteristica the caracteristica to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caracteristica,
     * or with status {@code 400 (Bad Request)} if the caracteristica is not valid,
     * or with status {@code 404 (Not Found)} if the caracteristica is not found,
     * or with status {@code 500 (Internal Server Error)} if the caracteristica couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Caracteristica> partialUpdateCaracteristica(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Caracteristica caracteristica
    ) throws URISyntaxException {
        log.debug("REST request to partial update Caracteristica partially : {}, {}", id, caracteristica);
        if (caracteristica.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, caracteristica.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!caracteristicaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Caracteristica> result = caracteristicaService.partialUpdate(caracteristica);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, caracteristica.getId().toString())
        );
    }

    /**
     * {@code GET  /caracteristicas} : get all the caracteristicas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of caracteristicas in body.
     */
    @GetMapping("")
    public List<Caracteristica> getAllCaracteristicas() {
        log.debug("REST request to get all Caracteristicas");
        return caracteristicaService.findAll();
    }

    /**
     * {@code GET  /caracteristicas/:id} : get the "id" caracteristica.
     *
     * @param id the id of the caracteristica to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the caracteristica, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Caracteristica> getCaracteristica(@PathVariable("id") Long id) {
        log.debug("REST request to get Caracteristica : {}", id);
        Optional<Caracteristica> caracteristica = caracteristicaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(caracteristica);
    }

    /**
     * {@code DELETE  /caracteristicas/:id} : delete the "id" caracteristica.
     *
     * @param id the id of the caracteristica to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCaracteristica(@PathVariable("id") Long id) {
        log.debug("REST request to delete Caracteristica : {}", id);
        caracteristicaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

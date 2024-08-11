package ar.edu.um.programacion2.trabajo_final.service.impl;

import ar.edu.um.programacion2.trabajo_final.domain.Personalizacion;
import ar.edu.um.programacion2.trabajo_final.repository.PersonalizacionRepository;
import ar.edu.um.programacion2.trabajo_final.service.PersonalizacionService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.programacion2.trabajo_final.domain.Personalizacion}.
 */
@Service
@Transactional
public class PersonalizacionServiceImpl implements PersonalizacionService {

    private static final Logger log = LoggerFactory.getLogger(PersonalizacionServiceImpl.class);

    private final PersonalizacionRepository personalizacionRepository;

    public PersonalizacionServiceImpl(PersonalizacionRepository personalizacionRepository) {
        this.personalizacionRepository = personalizacionRepository;
    }

    @Override
    public Personalizacion save(Personalizacion personalizacion) {
        log.debug("Request to save Personalizacion : {}", personalizacion);
        return personalizacionRepository.save(personalizacion);
    }

    @Override
    public Personalizacion update(Personalizacion personalizacion) {
        log.debug("Request to update Personalizacion : {}", personalizacion);
        return personalizacionRepository.save(personalizacion);
    }

    @Override
    public Optional<Personalizacion> partialUpdate(Personalizacion personalizacion) {
        log.debug("Request to partially update Personalizacion : {}", personalizacion);

        return personalizacionRepository
            .findById(personalizacion.getId())
            .map(existingPersonalizacion -> {
                if (personalizacion.getNombre() != null) {
                    existingPersonalizacion.setNombre(personalizacion.getNombre());
                }
                if (personalizacion.getDescripcion() != null) {
                    existingPersonalizacion.setDescripcion(personalizacion.getDescripcion());
                }

                return existingPersonalizacion;
            })
            .map(personalizacionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Personalizacion> findAll() {
        log.debug("Request to get all Personalizacions");
        return personalizacionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Personalizacion> findOne(Long id) {
        log.debug("Request to get Personalizacion : {}", id);
        return personalizacionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Personalizacion : {}", id);
        personalizacionRepository.deleteById(id);
    }
}

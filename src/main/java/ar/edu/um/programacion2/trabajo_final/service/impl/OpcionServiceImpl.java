package ar.edu.um.programacion2.trabajo_final.service.impl;

import ar.edu.um.programacion2.trabajo_final.domain.Opcion;
import ar.edu.um.programacion2.trabajo_final.repository.OpcionRepository;
import ar.edu.um.programacion2.trabajo_final.service.OpcionService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.programacion2.trabajo_final.domain.Opcion}.
 */
@Service
@Transactional
public class OpcionServiceImpl implements OpcionService {

    private static final Logger log = LoggerFactory.getLogger(OpcionServiceImpl.class);

    private final OpcionRepository opcionRepository;

    public OpcionServiceImpl(OpcionRepository opcionRepository) {
        this.opcionRepository = opcionRepository;
    }

    @Override
    public Opcion save(Opcion opcion) {
        log.debug("Request to save Opcion : {}", opcion);
        return opcionRepository.save(opcion);
    }

    @Override
    public Opcion update(Opcion opcion) {
        log.debug("Request to update Opcion : {}", opcion);
        return opcionRepository.save(opcion);
    }

    @Override
    public Optional<Opcion> partialUpdate(Opcion opcion) {
        log.debug("Request to partially update Opcion : {}", opcion);

        return opcionRepository
            .findById(opcion.getId())
            .map(existingOpcion -> {
                if (opcion.getCodigo() != null) {
                    existingOpcion.setCodigo(opcion.getCodigo());
                }
                if (opcion.getNombre() != null) {
                    existingOpcion.setNombre(opcion.getNombre());
                }
                if (opcion.getDescripcion() != null) {
                    existingOpcion.setDescripcion(opcion.getDescripcion());
                }
                if (opcion.getPrecioAdicional() != null) {
                    existingOpcion.setPrecioAdicional(opcion.getPrecioAdicional());
                }

                return existingOpcion;
            })
            .map(opcionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Opcion> findAll() {
        log.debug("Request to get all Opcions");
        return opcionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Opcion> findOne(Long id) {
        log.debug("Request to get Opcion : {}", id);
        return opcionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Opcion : {}", id);
        opcionRepository.deleteById(id);
    }
}

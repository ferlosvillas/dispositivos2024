package ar.edu.um.programacion2.trabajo_final.service.impl;

import ar.edu.um.programacion2.trabajo_final.domain.Adicional;
import ar.edu.um.programacion2.trabajo_final.repository.AdicionalRepository;
import ar.edu.um.programacion2.trabajo_final.service.AdicionalService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.programacion2.trabajo_final.domain.Adicional}.
 */
@Service
@Transactional
public class AdicionalServiceImpl implements AdicionalService {

    private static final Logger log = LoggerFactory.getLogger(AdicionalServiceImpl.class);

    private final AdicionalRepository adicionalRepository;

    public AdicionalServiceImpl(AdicionalRepository adicionalRepository) {
        this.adicionalRepository = adicionalRepository;
    }

    @Override
    public Adicional save(Adicional adicional) {
        log.debug("Request to save Adicional : {}", adicional);
        return adicionalRepository.save(adicional);
    }

    @Override
    public Adicional update(Adicional adicional) {
        log.debug("Request to update Adicional : {}", adicional);
        return adicionalRepository.save(adicional);
    }

    @Override
    public Optional<Adicional> partialUpdate(Adicional adicional) {
        log.debug("Request to partially update Adicional : {}", adicional);

        return adicionalRepository
            .findById(adicional.getId())
            .map(existingAdicional -> {
                if (adicional.getNombre() != null) {
                    existingAdicional.setNombre(adicional.getNombre());
                }
                if (adicional.getDescripcion() != null) {
                    existingAdicional.setDescripcion(adicional.getDescripcion());
                }
                if (adicional.getPrecio() != null) {
                    existingAdicional.setPrecio(adicional.getPrecio());
                }
                if (adicional.getPrecioGratis() != null) {
                    existingAdicional.setPrecioGratis(adicional.getPrecioGratis());
                }

                return existingAdicional;
            })
            .map(adicionalRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Adicional> findAll() {
        log.debug("Request to get all Adicionals");
        return adicionalRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Adicional> findOne(Long id) {
        log.debug("Request to get Adicional : {}", id);
        return adicionalRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Adicional : {}", id);
        adicionalRepository.deleteById(id);
    }
}

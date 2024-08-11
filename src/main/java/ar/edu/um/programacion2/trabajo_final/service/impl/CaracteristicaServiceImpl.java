package ar.edu.um.programacion2.trabajo_final.service.impl;

import ar.edu.um.programacion2.trabajo_final.domain.Caracteristica;
import ar.edu.um.programacion2.trabajo_final.repository.CaracteristicaRepository;
import ar.edu.um.programacion2.trabajo_final.service.CaracteristicaService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.programacion2.trabajo_final.domain.Caracteristica}.
 */
@Service
@Transactional
public class CaracteristicaServiceImpl implements CaracteristicaService {

    private static final Logger log = LoggerFactory.getLogger(CaracteristicaServiceImpl.class);

    private final CaracteristicaRepository caracteristicaRepository;

    public CaracteristicaServiceImpl(CaracteristicaRepository caracteristicaRepository) {
        this.caracteristicaRepository = caracteristicaRepository;
    }

    @Override
    public Caracteristica save(Caracteristica caracteristica) {
        log.debug("Request to save Caracteristica : {}", caracteristica);
        return caracteristicaRepository.save(caracteristica);
    }

    @Override
    public Caracteristica update(Caracteristica caracteristica) {
        log.debug("Request to update Caracteristica : {}", caracteristica);
        return caracteristicaRepository.save(caracteristica);
    }

    @Override
    public Optional<Caracteristica> partialUpdate(Caracteristica caracteristica) {
        log.debug("Request to partially update Caracteristica : {}", caracteristica);

        return caracteristicaRepository
            .findById(caracteristica.getId())
            .map(existingCaracteristica -> {
                if (caracteristica.getNombre() != null) {
                    existingCaracteristica.setNombre(caracteristica.getNombre());
                }
                if (caracteristica.getDescripcion() != null) {
                    existingCaracteristica.setDescripcion(caracteristica.getDescripcion());
                }

                return existingCaracteristica;
            })
            .map(caracteristicaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Caracteristica> findAll() {
        log.debug("Request to get all Caracteristicas");
        return caracteristicaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Caracteristica> findOne(Long id) {
        log.debug("Request to get Caracteristica : {}", id);
        return caracteristicaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Caracteristica : {}", id);
        caracteristicaRepository.deleteById(id);
    }
}

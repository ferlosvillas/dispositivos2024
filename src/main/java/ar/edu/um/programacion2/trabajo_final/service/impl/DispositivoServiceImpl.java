package ar.edu.um.programacion2.trabajo_final.service.impl;

import ar.edu.um.programacion2.trabajo_final.domain.Dispositivo;
import ar.edu.um.programacion2.trabajo_final.repository.DispositivoRepository;
import ar.edu.um.programacion2.trabajo_final.service.DispositivoService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.programacion2.trabajo_final.domain.Dispositivo}.
 */
@Service
@Transactional
public class DispositivoServiceImpl implements DispositivoService {

    private static final Logger log = LoggerFactory.getLogger(DispositivoServiceImpl.class);

    private final DispositivoRepository dispositivoRepository;

    public DispositivoServiceImpl(DispositivoRepository dispositivoRepository) {
        this.dispositivoRepository = dispositivoRepository;
    }

    @Override
    public Dispositivo save(Dispositivo dispositivo) {
        log.debug("Request to save Dispositivo : {}", dispositivo);
        return dispositivoRepository.save(dispositivo);
    }

    @Override
    public Dispositivo update(Dispositivo dispositivo) {
        log.debug("Request to update Dispositivo : {}", dispositivo);
        return dispositivoRepository.save(dispositivo);
    }

    @Override
    public Optional<Dispositivo> partialUpdate(Dispositivo dispositivo) {
        log.debug("Request to partially update Dispositivo : {}", dispositivo);

        return dispositivoRepository
            .findById(dispositivo.getId())
            .map(existingDispositivo -> {
                if (dispositivo.getCodigo() != null) {
                    existingDispositivo.setCodigo(dispositivo.getCodigo());
                }
                if (dispositivo.getNombre() != null) {
                    existingDispositivo.setNombre(dispositivo.getNombre());
                }
                if (dispositivo.getDescripcion() != null) {
                    existingDispositivo.setDescripcion(dispositivo.getDescripcion());
                }
                if (dispositivo.getPrecioBase() != null) {
                    existingDispositivo.setPrecioBase(dispositivo.getPrecioBase());
                }
                if (dispositivo.getMoneda() != null) {
                    existingDispositivo.setMoneda(dispositivo.getMoneda());
                }

                return existingDispositivo;
            })
            .map(dispositivoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Dispositivo> findAll() {
        log.debug("Request to get all Dispositivos");
        return dispositivoRepository.findAll();
    }

    public Page<Dispositivo> findAllWithEagerRelationships(Pageable pageable) {
        return dispositivoRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Dispositivo> findOne(Long id) {
        log.debug("Request to get Dispositivo : {}", id);
        return dispositivoRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dispositivo : {}", id);
        dispositivoRepository.deleteById(id);
    }
}

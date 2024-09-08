package ar.edu.um.programacion2.trabajo_final.service.impl;

import ar.edu.um.programacion2.trabajo_final.domain.Grupo;
import ar.edu.um.programacion2.trabajo_final.repository.GrupoRepository;
import ar.edu.um.programacion2.trabajo_final.service.GrupoService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.programacion2.trabajo_final.domain.Grupo}.
 */
@Service
@Transactional
public class GrupoServiceImpl implements GrupoService {

    private static final Logger log = LoggerFactory.getLogger(GrupoServiceImpl.class);

    private final GrupoRepository grupoRepository;

    public GrupoServiceImpl(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    @Override
    public Grupo save(Grupo grupo) {
        log.debug("Request to save Grupo : {}", grupo);
        return grupoRepository.save(grupo);
    }

    @Override
    public Grupo update(Grupo grupo) {
        log.debug("Request to update Grupo : {}", grupo);
        return grupoRepository.save(grupo);
    }

    @Override
    public Optional<Grupo> partialUpdate(Grupo grupo) {
        log.debug("Request to partially update Grupo : {}", grupo);

        return grupoRepository
            .findById(grupo.getId())
            .map(existingGrupo -> {
                if (grupo.getIdGrupo() != null) {
                    existingGrupo.setIdGrupo(grupo.getIdGrupo());
                }
                if (grupo.getNombres() != null) {
                    existingGrupo.setNombres(grupo.getNombres());
                }
                if (grupo.getDescripcion() != null) {
                    existingGrupo.setDescripcion(grupo.getDescripcion());
                }

                return existingGrupo;
            })
            .map(grupoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Grupo> findAll() {
        log.debug("Request to get all Grupos");
        return grupoRepository.findAll();
    }

    public Page<Grupo> findAllWithEagerRelationships(Pageable pageable) {
        return grupoRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Grupo> findOne(Long id) {
        log.debug("Request to get Grupo : {}", id);
        return grupoRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Grupo : {}", id);
        grupoRepository.deleteById(id);
    }
}

package ar.edu.um.programacion2.trabajo_final.repository;

import ar.edu.um.programacion2.trabajo_final.domain.Dispositivo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface DispositivoRepositoryWithBagRelationships {
    Optional<Dispositivo> fetchBagRelationships(Optional<Dispositivo> dispositivo);

    List<Dispositivo> fetchBagRelationships(List<Dispositivo> dispositivos);

    Page<Dispositivo> fetchBagRelationships(Page<Dispositivo> dispositivos);
}

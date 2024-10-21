package ar.edu.um.programacion2.trabajo_final.repository;

import ar.edu.um.programacion2.trabajo_final.domain.Grupo;
import ar.edu.um.programacion2.trabajo_final.domain.Venta;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaPersonalizadoRepository extends JpaRepository<Venta, Long> {
    List<Venta> findAllByGrupo(Grupo grupo);
    Optional<Venta> findByGrupoAndId(Grupo grupo, Long id);
}

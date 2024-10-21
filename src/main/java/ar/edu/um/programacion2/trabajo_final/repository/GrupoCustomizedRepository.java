package ar.edu.um.programacion2.trabajo_final.repository;

import ar.edu.um.programacion2.trabajo_final.domain.Grupo;
import ar.edu.um.programacion2.trabajo_final.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoCustomizedRepository extends JpaRepository<Grupo, Long> {
    Optional<Grupo> findOneByUser(User user);
}

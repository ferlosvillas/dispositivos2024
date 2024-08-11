package ar.edu.um.programacion2.trabajo_final.repository;

import ar.edu.um.programacion2.trabajo_final.domain.Personalizacion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Personalizacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonalizacionRepository extends JpaRepository<Personalizacion, Long> {}

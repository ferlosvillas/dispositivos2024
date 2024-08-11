package ar.edu.um.programacion2.trabajo_final.repository;

import ar.edu.um.programacion2.trabajo_final.domain.Adicional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Adicional entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdicionalRepository extends JpaRepository<Adicional, Long> {}

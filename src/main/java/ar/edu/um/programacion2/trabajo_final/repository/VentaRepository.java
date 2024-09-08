package ar.edu.um.programacion2.trabajo_final.repository;

import ar.edu.um.programacion2.trabajo_final.domain.Venta;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Venta entity.
 */
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    default Optional<Venta> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Venta> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Venta> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select venta from Venta venta left join fetch venta.grupo", countQuery = "select count(venta) from Venta venta")
    Page<Venta> findAllWithToOneRelationships(Pageable pageable);

    @Query("select venta from Venta venta left join fetch venta.grupo")
    List<Venta> findAllWithToOneRelationships();

    @Query("select venta from Venta venta left join fetch venta.grupo where venta.id =:id")
    Optional<Venta> findOneWithToOneRelationships(@Param("id") Long id);
}

package ar.edu.um.programacion2.trabajo_final.repository;

import ar.edu.um.programacion2.trabajo_final.domain.Grupo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Grupo entity.
 */
@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    default Optional<Grupo> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Grupo> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Grupo> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select grupo from Grupo grupo left join fetch grupo.user", countQuery = "select count(grupo) from Grupo grupo")
    Page<Grupo> findAllWithToOneRelationships(Pageable pageable);

    @Query("select grupo from Grupo grupo left join fetch grupo.user")
    List<Grupo> findAllWithToOneRelationships();

    @Query("select grupo from Grupo grupo left join fetch grupo.user where grupo.id =:id")
    Optional<Grupo> findOneWithToOneRelationships(@Param("id") Long id);
}

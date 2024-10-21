package ar.edu.um.programacion2.trabajo_final.repository;

import ar.edu.um.programacion2.trabajo_final.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCustomizedRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByEmailIgnoreCaseAndLogin(String email, String login);
}

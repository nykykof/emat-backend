package ci.csi.emat.domain.user.repository;

import ci.csi.emat.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);
    UserEntity findByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

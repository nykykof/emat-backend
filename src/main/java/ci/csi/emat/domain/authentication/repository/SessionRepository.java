package ci.csi.emat.domain.authentication.repository;

import ci.csi.emat.domain.authentication.entity.UserSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<UserSessionEntity, Long> {

    Optional<UserSessionEntity> findByUserId(Long id);
}

package ci.csi.emat.domain.user.repository;

import ci.csi.emat.domain.user.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

    Optional<PermissionEntity> findByCode(String code);

    boolean existsByCode(String code);
}

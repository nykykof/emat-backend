package ci.csi.emat.domain.user.mapper;

import ci.csi.emat.domain.user.dto.PermissionDTO;
import ci.csi.emat.domain.user.entity.PermissionEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermissionMapper {

    public PermissionEntity toEntity(PermissionDTO permission) {

        return PermissionEntity.builder()
                .code(permission.getCode())
                .label(permission.getLabel())
                .build();
    }

    public PermissionDTO toDTO(PermissionEntity permissionEntity) {
        return PermissionDTO.builder()
                .id(permissionEntity.getId())
                .label(permissionEntity.getLabel())
                .code(permissionEntity.getCode())
                .build();
    }

    public List<PermissionDTO> toDTOs(List<PermissionEntity> permissionEntities) {
        return permissionEntities.stream()
                .map(this::toDTO)
                .toList();
    }
}

package ci.csi.emat.domain.user.mapper;

import ci.csi.emat.domain.user.dto.RoleDTO;
import ci.csi.emat.domain.user.entity.RoleEntity;
import ci.csi.emat.domain.user.form.RoleCreationForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleMapper {

    private final PermissionMapper permissionMapper;

    public RoleEntity toEntity(RoleCreationForm role) {
        return RoleEntity.builder()
                .label(role.getLabel())
                .code(role.getCode())
                .description(role.getDescription())
                .build();
    }

    public RoleDTO toDto(RoleEntity role) {
        return RoleDTO.builder()
                .id(role.getId())
                .label(role.getLabel())
                .code(role.getCode())
                .description(role.getDescription())
                .permissions(permissionMapper.toDTOs(role.getPermissions()))
                .build();
    }

    public List<RoleDTO> toDtos(List<RoleEntity> roles) {
        return roles.stream()
                .map(this::toDto)
                .toList();
    }
}

package ci.csi.emat.domain.user.service;

import ci.csi.emat.domain.user.dto.RoleDTO;
import ci.csi.emat.domain.user.entity.PermissionEntity;
import ci.csi.emat.domain.user.entity.RoleEntity;
import ci.csi.emat.domain.user.error.exception.RoleNotFoundException;
import ci.csi.emat.domain.user.form.RoleCreationForm;
import ci.csi.emat.domain.user.mapper.PermissionMapper;
import ci.csi.emat.domain.user.mapper.RoleMapper;
import ci.csi.emat.domain.user.repository.PermissionRepository;
import ci.csi.emat.domain.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    private final PermissionRepository permissionRepository;

    public RoleDTO getRole(String code) {
        return roleRepository.findByCode(code)
                .map(roleMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("Role code {} not found", code);
                    return new RoleNotFoundException();
                });
    }

    public List<RoleDTO> getRoles() {
        return roleMapper.toDtos(roleRepository.findAll());
    }

    public RoleDTO createRole(RoleCreationForm form) {
        RoleEntity roleEntity = roleMapper.toEntity(form);
        List<PermissionEntity> validPermissions = form.getPermissions()
                .stream()
                .map(permissionDTO -> permissionRepository.findByCode(permissionDTO.getCode()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        roleEntity.setPermissions(validPermissions);
        return roleMapper.toDto(roleRepository.save(roleEntity));
    }
}

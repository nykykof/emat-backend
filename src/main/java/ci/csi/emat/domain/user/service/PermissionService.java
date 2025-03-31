package ci.csi.emat.domain.user.service;

import ci.csi.emat.domain.user.dto.PermissionDTO;
import ci.csi.emat.domain.user.error.exception.PermissionNotFoundException;
import ci.csi.emat.domain.user.mapper.PermissionMapper;
import ci.csi.emat.domain.user.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public List<PermissionDTO> getPermissions() {

        return this.permissionMapper
                .toDTOs(this.permissionRepository
                        .findAll());
    }

    public PermissionDTO getPermission(String code) {
        return permissionRepository.findByCode(code)
                .map(permissionMapper::toDTO)
                .orElseThrow(() -> {
                    log.warn("Permission code {} not found", code);
                    return new PermissionNotFoundException();
                });
    }
}

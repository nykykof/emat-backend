package ci.csi.emat.domain.user.controller;

import ci.csi.emat.domain.user.dto.PermissionDTO;
import ci.csi.emat.domain.user.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping("/{code}")
    public ResponseEntity<PermissionDTO> getPermission(@PathVariable String code) {
        log.info("Get permission for code {}", code);
        return ResponseEntity.ok(permissionService.getPermission(code));
    }

    @GetMapping
    public ResponseEntity<List<PermissionDTO>> getPermissions() {
        log.info("Get permissions");
        return ResponseEntity.ok(permissionService.getPermissions());
    }
}

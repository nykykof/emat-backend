package ci.csi.emat.domain.user.controller;

import ci.csi.emat.domain.user.dto.RoleDTO;
import ci.csi.emat.domain.user.form.RoleCreationForm;
import ci.csi.emat.domain.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public List<RoleDTO> getRoles() {
        log.info("getRoles");
        return roleService.getRoles();
    }

    @GetMapping("/{code}")
    public RoleDTO getRole(@PathVariable String code) {
        log.info("getRole {}", code);
        return roleService.getRole(code);
    }

    @PostMapping
    public RoleDTO createRole(@RequestBody RoleCreationForm form) {
        log.info("createRole {}", form);
        return roleService.createRole(form);
    }
}

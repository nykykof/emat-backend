package ci.csi.emat.domain.user.form;

import ci.csi.emat.domain.user.dto.PermissionDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoleCreationForm {

    private String code;
    private String label;
    private String description;
    private List<PermissionDTO> permissions;
}

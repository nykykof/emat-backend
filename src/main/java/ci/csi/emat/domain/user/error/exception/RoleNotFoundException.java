package ci.csi.emat.domain.user.error.exception;

import ci.csi.emat.common.exception.BusinessException;

import static ci.csi.emat.domain.user.error.RoleErrors.ROLE_001;

public class RoleNotFoundException extends BusinessException {
    public RoleNotFoundException() {
        super(ROLE_001);
    }
}

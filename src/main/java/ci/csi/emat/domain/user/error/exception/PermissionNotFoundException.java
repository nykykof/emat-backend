package ci.csi.emat.domain.user.error.exception;

import ci.csi.emat.common.exception.BusinessException;

import static ci.csi.emat.domain.user.error.PermissionErrors.PEM_001;

public class PermissionNotFoundException extends BusinessException {

    public PermissionNotFoundException() {
        super(PEM_001);
    }
}

package ci.csi.emat.domain.user.error;

import ci.csi.emat.common.advice.error.ErrorDetails;
import lombok.Getter;

@Getter
public enum RoleErrors implements ErrorDetails {

    ROLE_001("Role code not found", "code");

    private String errorMessage;
    private String errorField;

    RoleErrors(String errorMessage, String errorField) {
        this.errorMessage = errorMessage;
        this.errorField = errorField;
    }

    @Override
    public String getErrorCode() {
        return this.name();
    }
}

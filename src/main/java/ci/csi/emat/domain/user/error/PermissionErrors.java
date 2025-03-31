package ci.csi.emat.domain.user.error;

import ci.csi.emat.common.advice.error.ErrorDetails;
import lombok.Getter;

@Getter
public enum PermissionErrors implements ErrorDetails {
    PEM_001("Permission code not found", "code");

    private String errorMessage;
    private String errorField;

    PermissionErrors(String errorMessage, String errorField) {
        this.errorMessage = errorMessage;
        this.errorField = errorField;
    }

    @Override
    public String getErrorCode() {
        return this.name();
    }
}

package ci.csi.emat.domain.user.error;

import ci.csi.emat.common.advice.error.ErrorDetails;
import lombok.Getter;

@Getter
public enum UserErrors implements ErrorDetails {

    USR_001("User id not found", "userId");

    private String errorMessage;
    private String errorField;

    UserErrors(String defaultMessage, String field) {
        this.errorMessage = defaultMessage;
        this.errorField = field;
    }

    @Override
    public String getErrorCode() {
        return this.name();
    }
}

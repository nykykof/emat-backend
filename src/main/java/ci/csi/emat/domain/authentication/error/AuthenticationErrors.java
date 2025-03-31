package ci.csi.emat.domain.authentication.error;

import ci.csi.emat.common.advice.error.ErrorDetails;
import lombok.Getter;

@Getter
public enum AuthenticationErrors implements ErrorDetails {

    AUTH_001("User not found with provided information", "username"),
    AUTH_002("Invalid username or password", "username | password"),
    AUTH_003("Unable to process provided JWT", "Bearer token");

    private final String errorMessage;
    private final String errorField;

    AuthenticationErrors(String defaultMessage, String field) {
        this.errorMessage = defaultMessage;
        this.errorField = field;
    }

    @Override
    public String getErrorCode() {
        return this.name();
    }
}

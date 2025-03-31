package ci.csi.emat.common.exception;

import ci.csi.emat.common.advice.error.ErrorDetails;
import lombok.Getter;

@Getter
public enum DefaultErrorDetails implements ErrorDetails {

    DEFAULT_000("An error occurred while processing your request", "");

    private final String errorMessage;
    private final String errorField;

    DefaultErrorDetails(String defaultMessage, String field) {
        this.errorMessage = defaultMessage;
        this.errorField = field;
    }

    @Override
    public String getErrorCode() {
        return this.name();
    }
}

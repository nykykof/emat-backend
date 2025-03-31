package ci.csi.emat.common.exception;

import ci.csi.emat.common.advice.error.ErrorDetails;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Objects;

import static ci.csi.emat.common.exception.DefaultErrorDetails.DEFAULT_000;

@Getter
@Setter
public class BusinessException extends RuntimeException {

    private String errorCode;
    private String errorField;
    private String errorMessage;
    private HttpStatus httpStatus;

    public BusinessException() {
        throw new BusinessException(DEFAULT_000, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public BusinessException(ErrorDetails errorDetails) {
        super(errorDetails.getErrorMessage());
        this.errorCode = errorDetails.getErrorCode();
        this.errorMessage = errorDetails.getErrorMessage();
        this.errorField = errorDetails.getErrorField();

        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public BusinessException(ErrorDetails errorDetails, HttpStatus httpStatus) {
        super(errorDetails.getErrorMessage());
        this.errorCode = errorDetails.getErrorCode();
        this.errorMessage = errorDetails.getErrorMessage();
        this.errorField = errorDetails.getErrorField();
        this.httpStatus = httpStatus;

    }

    public BusinessException(Throwable throwable, HttpStatus httpStatus, ErrorDetails errorDetails) {
        super(errorDetails.getErrorMessage(), throwable);
        this.errorCode = errorDetails.getErrorCode();
        this.errorMessage = errorDetails.getErrorMessage();
        this.errorField = errorDetails.getErrorField();
        if (Objects.nonNull(httpStatus)) {
            this.httpStatus = httpStatus;
        } else {
            this.httpStatus = HttpStatus.BAD_REQUEST;
        }
    }

}

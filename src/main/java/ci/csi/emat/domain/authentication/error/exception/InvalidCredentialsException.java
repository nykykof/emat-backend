package ci.csi.emat.domain.authentication.error.exception;

import ci.csi.emat.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

import static ci.csi.emat.domain.authentication.error.AuthenticationErrors.AUTH_002;

public class InvalidCredentialsException extends BusinessException {

    public InvalidCredentialsException() {
        super(AUTH_002, HttpStatus.UNAUTHORIZED);
    }
}

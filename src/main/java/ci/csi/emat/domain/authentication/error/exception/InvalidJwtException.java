package ci.csi.emat.domain.authentication.error.exception;

import ci.csi.emat.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

import static ci.csi.emat.domain.authentication.error.AuthenticationErrors.AUTH_003;

public class InvalidJwtException extends BusinessException {

    public InvalidJwtException() {
        super(AUTH_003, HttpStatus.UNAUTHORIZED);
    }
}

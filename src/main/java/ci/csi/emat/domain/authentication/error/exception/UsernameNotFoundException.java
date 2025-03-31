package ci.csi.emat.domain.authentication.error.exception;

import ci.csi.emat.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

import static ci.csi.emat.domain.authentication.error.AuthenticationErrors.AUTH_001;

public class UsernameNotFoundException extends BusinessException {

    public UsernameNotFoundException() {
        super(AUTH_001, HttpStatus.UNAUTHORIZED);
    }

    public UsernameNotFoundException(HttpStatus httpStatus) {
        super(AUTH_001, httpStatus);
    }
}

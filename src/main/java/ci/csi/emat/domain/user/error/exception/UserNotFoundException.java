package ci.csi.emat.domain.user.error.exception;

import ci.csi.emat.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

import static ci.csi.emat.domain.user.error.UserErrors.USER_001;

public class UserNotFoundException extends BusinessException {


    public UserNotFoundException() {
        super(USER_001, HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(HttpStatus httpStatus) {
        super(USER_001, httpStatus);
    }
}

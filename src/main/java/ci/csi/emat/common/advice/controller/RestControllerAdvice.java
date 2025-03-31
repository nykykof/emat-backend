package ci.csi.emat.common.advice.controller;

import ci.csi.emat.common.advice.error.ApiError;
import ci.csi.emat.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> processError(BusinessException exception, HttpServletRequest httpServletRequest) {

        return ResponseEntity.status(exception.getHttpStatus())
                .body(ApiError.builder()
                        .businessErrorMessage(exception.getErrorMessage())
                        .field(exception.getErrorField())
                        .businessErrorCode(exception.getErrorCode())
                        .path(httpServletRequest.getRequestURI())
                        .build());
    }
}

package ci.csi.emat.domain.authentication.controller;

import ci.csi.emat.domain.authentication.dto.LoginResponseDTO;
import ci.csi.emat.domain.authentication.error.exception.InvalidCredentialsException;
import ci.csi.emat.domain.authentication.form.LoginForm;
import ci.csi.emat.domain.authentication.service.AuthenticationService;
import ci.csi.emat.domain.authentication.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final SessionService sessionService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginForm form) {

        log.info("attempt to log user {} in ...", form.getUsername());
        try {
            // test user credentials
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword()));
        } catch (Exception e) {
            log.error("Authentication failed for user {}", form.getUsername());
            throw new InvalidCredentialsException();
        }
        return authenticationService.login(form);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String AuthorisationHeaderValue) {
        String token = AuthorisationHeaderValue.substring("Bearer ".length());
        String username = authenticationService.extractUsername(token);
        log.info("attempt to log user {} out ...", username);
        Long sessionId = Long.parseLong(authenticationService.extractSessionId(token));
        sessionService.endSession(sessionId);
        log.info("User {} has been logged out", username);
        return ResponseEntity.accepted().build();
    }
}

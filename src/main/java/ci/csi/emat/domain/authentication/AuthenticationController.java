package ci.csi.emat.domain.authentication;

import ci.csi.emat.domain.authentication.dto.LoginResponseDTO;
import ci.csi.emat.domain.authentication.form.LoginForm;
import ci.csi.emat.domain.authentication.service.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(LoginForm form){

        // login logic
        return authenticationService.login(form);
    }
}

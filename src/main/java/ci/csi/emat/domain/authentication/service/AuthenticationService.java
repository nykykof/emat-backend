package ci.csi.emat.domain.authentication.service;

import ci.csi.emat.domain.authentication.dto.LoginResponseDTO;
import ci.csi.emat.domain.authentication.form.LoginForm;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public LoginResponseDTO login(LoginForm form){

        // TODO: implement login logic

        return new LoginResponseDTO();
    }
}

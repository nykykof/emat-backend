package ci.csi.emat.domain.user.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationForm {
    private String firstname;
    private String lastname;
    private String username;
    private String password;
}

package ci.csi.emat.utils;

import ci.csi.emat.domain.user.form.UserCreationForm;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    public static String generateUserEmail(UserCreationForm form) {
        return form.getFirstname().toLowerCase() + "." + form.getLastname().toLowerCase() + "@csi.ci";
    }
}

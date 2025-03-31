package ci.csi.emat.domain.user.mapper;

import ci.csi.emat.domain.user.dto.UserDTO;
import ci.csi.emat.domain.user.entity.UserEntity;
import ci.csi.emat.domain.user.form.UserCreationForm;
import ci.csi.emat.utils.UserUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public UserDTO toDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setFirstname(userEntity.getFirstname());
        userDTO.setLastname(userEntity.getLastname());
        userDTO.setEmail(userEntity.getEmail());
        return userDTO;
    }

    public UserEntity toEntity(UserCreationForm form) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(form.getUsername());
        userEntity.setFirstname(form.getFirstname());
        userEntity.setLastname(form.getLastname());
        userEntity.setPassword(form.getPassword());
        userEntity.setEmail(UserUtils.generateUserEmail(form));
        return userEntity;
    }
}

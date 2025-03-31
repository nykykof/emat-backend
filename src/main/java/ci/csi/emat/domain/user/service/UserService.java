package ci.csi.emat.domain.user.service;

import ci.csi.emat.common.exception.BusinessException;
import ci.csi.emat.domain.user.dto.UserDTO;
import ci.csi.emat.domain.user.entity.UserEntity;
import ci.csi.emat.domain.user.error.exception.UserNotFoundException;
import ci.csi.emat.domain.user.form.UserCreationForm;
import ci.csi.emat.domain.user.form.UserUpdateForm;
import ci.csi.emat.domain.user.mapper.UserMapper;
import ci.csi.emat.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void updateLastLogin(String userName) throws BusinessException {
        this.userRepository.findByUsername(userName)
                .ifPresentOrElse(user -> {
                    user.setLastLogin(Instant.now());
                    this.userRepository.save(user);
                    log.info("User {} last login updated", userName);
                }, () -> log.warn("User {} not found", userName));

    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public UserDTO createUser(UserCreationForm form) {

        UserEntity user = this.userMapper.toEntity(form);

        UserEntity save = userRepository.save(user);

        return this.userMapper.toDTO(save);

    }

    public List<UserDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();

        return users.stream()
                .map(this.userMapper::toDTO)
                .toList();
    }

    public UserDTO update(Long userId, UserUpdateForm form) {

        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            log.warn("User with id {} not found", userId);
            throw new UserNotFoundException();
        }
        UserEntity user = optionalUser.get();
        user.setFirstname(form.getFirstname());
        user.setLastname(form.getLastname());

        UserEntity savedUser = userRepository.save(user);
        log.info("User with id {} updated", userId);
        return this.userMapper.toDTO(savedUser);
    }

    public void delete(Long userId) {

        if (!userRepository.existsById(userId)) {
            log.warn("User with id {} not found", userId);
            throw new UserNotFoundException();
        }
        userRepository.deleteById(userId);
    }

    public UserDTO getUser(Long userId) {

        UserEntity user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        return this.userMapper.toDTO(user);
    }
}

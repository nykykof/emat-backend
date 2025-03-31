package ci.csi.emat.domain.authentication.service;

import ci.csi.emat.common.exception.BusinessException;
import ci.csi.emat.domain.authentication.error.exception.UsernameNotFoundException;
import ci.csi.emat.domain.user.entity.UserEntity;
import ci.csi.emat.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserEntity loadUserByUsername(String username) throws BusinessException {
        try {
            Optional<UserEntity> user = userRepository.findByUsername(username);
            if (user.isEmpty()) {
                log.error("user {} not found in data base", username);
                // Do not do this. You are giving too much information about authentication error
                // instead return a generic error.
                throw new UsernameNotFoundException();
            }
            return user.get();
        } catch (Exception e) {
            log.error("An error occurred while processing authentication: {}", e.getMessage());
            throw new BusinessException();
        }
    }
}

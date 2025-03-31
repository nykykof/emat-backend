package ci.csi.emat.domain.authentication.service;

import ci.csi.emat.common.exception.BusinessException;
import ci.csi.emat.domain.authentication.entity.UserSessionEntity;
import ci.csi.emat.domain.authentication.repository.SessionRepository;
import ci.csi.emat.domain.user.entity.UserEntity;
import ci.csi.emat.domain.user.error.exception.UserNotFoundException;
import ci.csi.emat.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public Long startSession(String username) throws BusinessException {

        UserEntity user = this.userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        UserSessionEntity session = UserSessionEntity
                .builder()
                .userId(user.getId())
                .sessionStartTime(LocalDateTime.now())
                .build();
        return sessionRepository.save(session).getId();
    }

    public void endSession(Long sessionId) throws BusinessException {
        try {
            sessionRepository.findById(sessionId)
                    .ifPresent(session -> {
                        session.setSessionEndTime(LocalDateTime.now());
                        sessionRepository.save(session);
                    });
        } catch (Exception e) {
            throw new BusinessException();
        }
    }

    public boolean existsById(Long id) throws BusinessException {
        try {
            return sessionRepository.existsById(id);
        } catch (Exception e) {
            throw new BusinessException();
        }
    }

    public Optional<UserSessionEntity> getBySessionId(Long sessionId) throws BusinessException {
        try {
            return sessionRepository.findById(sessionId);
        } catch (Exception e) {
            throw new BusinessException();
        }
    }
}

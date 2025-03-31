package ci.csi.emat.configuration;

import ci.csi.emat.domain.authentication.entity.UserSessionEntity;
import ci.csi.emat.domain.authentication.service.AuthenticationService;
import ci.csi.emat.domain.authentication.service.SessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthenticationService authService;

    private final SessionService sessionService;

    public static final String BEARER_HEADER = "Bearer ";

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.extractTokenFromHeaders(request);
        try {
            if (StringUtils.isNotBlank(token)) {
                String sessionId = authService.extractSessionId(token);
                Optional<UserSessionEntity> optionalWebSession = sessionService.getBySessionId(Long.parseLong(sessionId));
                if (optionalWebSession.isPresent()) {
                    if (Objects.isNull(optionalWebSession.get().getSessionEndTime())) {
                        UsernamePasswordAuthenticationToken authentication = authService.getAuthentication(token);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        log.warn("session {} expired since {}", sessionId, optionalWebSession.get().getSessionEndTime());
                    }
                } else {
                    log.error("session {} not found", sessionId);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

    public String extractTokenFromHeaders(HttpServletRequest servletRequest) {
        Optional<String> authorization = Optional.ofNullable(servletRequest.getHeader("Authorization"));
        return authorization.map(s -> s.substring(BEARER_HEADER.length())).orElse("");
    }
}

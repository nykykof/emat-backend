package ci.csi.emat.domain.authentication.service;

import ci.csi.emat.common.exception.BusinessException;
import ci.csi.emat.configuration.properties.SecurityProperties;
import ci.csi.emat.domain.authentication.dto.CustomClaims;
import ci.csi.emat.domain.authentication.dto.LoginResponseDTO;
import ci.csi.emat.domain.authentication.error.exception.InvalidJwtException;
import ci.csi.emat.domain.authentication.form.LoginForm;
import ci.csi.emat.domain.user.entity.UserEntity;
import ci.csi.emat.domain.user.service.UserService;
import ci.csi.emat.utils.FunctionalUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final SecurityProperties securityProperties;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final SessionService sessionService;
    private final UserService userService;


    public LoginResponseDTO login(LoginForm form) {

        this.sessionService.startSession(form.getUsername());
        this.userService.updateLastLogin(form.getUsername());
        LoginResponseDTO token = this.generateToken(form.getUsername());
        log.info("Authentication succeeded. User {} is connected", form.getUsername());
        return token;
    }

    public String extractUsername(String token) {
        return extractClaim(token, (Claims claims) -> claims.get(CustomClaims.USERNAME, String.class));
    }

    public String extractSessionId(String token) {
        return extractClaim(token, Claims::getId);
    }

    public String extractIssuer(String token) {
        return extractClaim(token, Claims::getIssuer);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {

            final Claims claims = Jwts.parser().setSigningKey(this.securityProperties.getSecret()).parseClaimsJws(token).getBody();
            return claimsResolver.apply(claims);
        } catch (SignatureException e) {
            log.error("Unable to extract claims from token due to JWT Signature Mismatch ");
            throw new InvalidJwtException();
        } catch (MalformedJwtException e) {
            log.error("Unable to extract claims from token due to Malformed JWT");
            throw new InvalidJwtException();
        } catch (ExpiredJwtException e) {
            log.error("Unable to extract claims from token due to expired JWT provided");
            throw new InvalidJwtException();
        }
    }


    public Boolean tokenHasNotExpired(String token) {
        return extractExpiration(token).after(new Date());
    }

    public LoginResponseDTO generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        UserEntity user = this.userDetailsServiceImpl.loadUserByUsername(username);


        claims.put(CustomClaims.USERNAME, username);
        claims.put(CustomClaims.LAST_LOGIN,
                FunctionalUtils.getOrEmpty(() -> user.getLastLogin().toString()).orElse(""));

        //set user permissions
        Optional.ofNullable(user.getAuthorities())
                .ifPresent(grantedAuthorities -> {
                    List<String> permissions = user.getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList());
                    claims.put(CustomClaims.PERMISSIONS, permissions);
                });

        long startDate = System.currentTimeMillis();
        long tokenDuration = Long.sum(startDate, this.securityProperties.getTokenLifeSpan());

        Long sessionId = sessionService.startSession(username);

        String tokenStringValue = Jwts
                .builder()
                .setClaims(claims)
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(new Date(startDate))
                .setExpiration(new Date(tokenDuration))
                .setIssuer(this.securityProperties.getIssuer())
                .setId(String.valueOf(sessionId))
                .signWith(SignatureAlgorithm.HS256, this.securityProperties.getSecret()).compact();


        return LoginResponseDTO.builder()
                .accessToken(tokenStringValue)
                .expiredAt(tokenDuration)
                .lastLogin(FunctionalUtils.getOrNull(() -> user.getLastLogin().toString()))
                .build();
    }

    private boolean isIssuedByMe(String token) {
        String issuer = extractIssuer(token);
        return this.securityProperties.getIssuer().equals(issuer);
    }

    public Boolean validateToken(String token) {

        try {
            return tokenHasNotExpired(token) && isIssuedByMe(token);
        } catch (ExpiredJwtException ex) {
            log.warn("token has expired");
            return false;
        } catch (SignatureException ex) {
            log.warn("signature is not valid");
            return false;
        } catch (Exception ex) {
            log.warn("token not valid");
            return false;
        }
    }


    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String username = this.extractUsername(token);
        if (Objects.isNull(username)) {
            throw new IllegalArgumentException("cannot extract username");
        }
        UserEntity userDetails = this.userDetailsServiceImpl.loadUserByUsername(username);
        if (this.validateToken(token)) {
            return new UsernamePasswordAuthenticationToken(username, token, userDetails.getAuthorities());
        }
        log.error("cannot load user with username {}", username);
        throw new BusinessException();
    }
}

package ci.csi.emat.configuration;

import ci.csi.emat.domain.user.entity.PermissionEntity;
import ci.csi.emat.domain.user.entity.RoleEntity;
import ci.csi.emat.domain.user.entity.UserEntity;
import ci.csi.emat.domain.user.repository.PermissionRepository;
import ci.csi.emat.domain.user.repository.RoleRepository;
import ci.csi.emat.domain.user.repository.UserRepository;
import ci.csi.emat.utils.ParameterUtils;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
@ConditionalOnProperty(value = "initialize-data", havingValue = "true")
public class InitParameters implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        this.initUsers();
        this.initPermissions();
        this.initRoles();
    }

    private void initUsers() throws IOException {
        log.info("Initializing users...");
        JsonNode rootNode = ParameterUtils.getJsonNode("parameters/users.json");
        List<UserEntity> users = new ArrayList<>();

        rootNode.elements().forEachRemaining(node -> {
            String password = node.get("password").asText();
            String firstName = node.get("firstname").asText();
            String lastName = node.get("lastname").asText();
            String username = node.get("username").asText();
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@csi.ci";
            boolean enabled = node.get("enabled").asBoolean();
            boolean locked = node.get("locked").asBoolean();
            boolean accountExpired = node.get("accountExpired").asBoolean();
            boolean credentialsExpired = node.get("credentialsExpired").asBoolean();

            UserEntity userToBeSaved = UserEntity
                    .builder()
                    .enabled(enabled)
                    .firstname(firstName)
                    .lastname(lastName)
                    .password(this.passwordEncoder.encode(password))
                    .email(email)
                    .accountExpired(accountExpired)
                    .credentialsExpired(credentialsExpired)
                    .locked(locked)
                    .username(username)
                    .build();
            Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
            if (optionalUser.isEmpty()) {
                users.add(userToBeSaved);
            } else {
                userToBeSaved = optionalUser.get();
                userToBeSaved.setFirstname(firstName);
                userToBeSaved.setLastname(lastName);
                userToBeSaved.setUsername(username);
                userToBeSaved.setEmail(email);
                userToBeSaved.setPassword(this.passwordEncoder.encode(password));
                userToBeSaved.setAccountExpired(accountExpired);
                userToBeSaved.setCredentialsExpired(credentialsExpired);
                userToBeSaved.setLocked(locked);
                users.add(userToBeSaved);
            }
        });
        userRepository.saveAll(users);
        log.info("Users initialized.");
    }

    private void initPermissions() throws IOException {
        log.info("Initializing permissions...");
        JsonNode rootNode = ParameterUtils.getJsonNode("parameters/permissions.json");

        List<PermissionEntity> permissions = new ArrayList<>();

        rootNode.elements().forEachRemaining(node -> {
            String code = node.get("code").asText();
            String label = node.get("label").asText();
            PermissionEntity permission = PermissionEntity.builder()
                    .code(code)
                    .label(label)
                    .build();
            Optional<PermissionEntity> optionalPermission = permissionRepository.findByCode(code);
            if (optionalPermission.isEmpty()) {
                permissions.add(permission);
            } else {
                permission = optionalPermission.get();
                permission.setLabel(label);
                permissions.add(permission);
            }
        });
        log.info("Permissions initialized.");
        permissionRepository.saveAll(permissions);
    }

    private void initRoles() throws IOException {
        log.info("Initializing roles...");
        JsonNode rootNode = ParameterUtils.getJsonNode("parameters/roles.json");
        List<RoleEntity> roles = new ArrayList<>();
        rootNode.elements().forEachRemaining(node -> {
            String code = node.get("code").asText();
            String label = node.get("label").asText();
            String description = node.get("description").asText();

            List<PermissionEntity> permissions = ParameterUtils.getValuesWithType(node.get("permissions"), PermissionEntity.class);
            List<PermissionEntity> validPermissions = permissions.stream()
                    .map(perm -> permissionRepository.findByCode(perm.getCode()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            RoleEntity roleToCreate = RoleEntity.builder()
                    .description(description)
                    .label(label)
                    .code(code)
                    .permissions(validPermissions)
                    .build();
            Optional<RoleEntity> optionalRole = roleRepository.findByCode(code);
            if (optionalRole.isEmpty()) {
                roles.add(roleToCreate);
            } else {
                roleToCreate = optionalRole.get();
                roleToCreate.setDescription(description);
                roleToCreate.setLabel(label);
                //roleToCreate.setPermissions(validPermissions);
                roles.add(roleToCreate);
            }
        });
        roleRepository.saveAll(roles);
        log.info("Roles initialized.");
    }
}

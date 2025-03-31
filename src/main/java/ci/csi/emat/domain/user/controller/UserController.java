package ci.csi.emat.domain.user.controller;

import ci.csi.emat.domain.user.dto.UserDTO;
import ci.csi.emat.domain.user.form.UserCreationForm;
import ci.csi.emat.domain.user.form.UserUpdateForm;
import ci.csi.emat.domain.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreationForm form) {

        if (userService.existsByUsername(form.getUsername())) {
            log.warn("Username {} already exists", form.getUsername());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(form));
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        log.info("Get all users");
        return userService.getAllUsers();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody UserUpdateForm form) {
        UserDTO updatedUser = this.userService.update(userId, form);
        log.info("Update user {}", userId);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        log.info("Get user {}", userId);
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        log.info("Delete user {}", userId);
        userService.delete(userId);

        return ResponseEntity.noContent().build();
    }
}

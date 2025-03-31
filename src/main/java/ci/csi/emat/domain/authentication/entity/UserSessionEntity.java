package ci.csi.emat.domain.authentication.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_session")
public class UserSessionEntity implements Serializable {

    // FIX ME: use uuid instead
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;

    private LocalDateTime sessionStartTime;

    private LocalDateTime sessionEndTime;
}

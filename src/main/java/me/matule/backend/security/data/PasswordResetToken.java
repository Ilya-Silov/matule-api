package me.matule.backend.security.data;

import jakarta.persistence.*;
import lombok.*;
import me.matule.backend.data.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token; // Теперь это будет 6-значный код

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(5);
    private LocalDateTime nextRequestDate = LocalDateTime.now().plusMinutes(1);; // Через минуту можно запросить новый

    public PasswordResetToken(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
    public boolean canRequestNew() {
        return LocalDateTime.now().isAfter(nextRequestDate);
    }
}
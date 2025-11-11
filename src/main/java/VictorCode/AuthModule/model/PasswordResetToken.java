package VictorCode.AuthModule.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token; // UUID em texto

    @Column(nullable = false)
    private Instant expiryDate; // data de expiração

    @Column(nullable = false)
    private boolean used = false; // evita reuso

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public PasswordResetToken(String token, Instant expiryDate, User user) {
        this.token = token;
        this.expiryDate = expiryDate;
        this.user = user;
        this.used = false;
    }


    public boolean isExpired() {
        return Instant.now().isAfter(expiryDate);
    }

}

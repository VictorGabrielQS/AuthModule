package VictorCode.AuthModule.dto;

import lombok.Data;

@Data

public class PasswordResetConfirmRequest {
    private String token;
    private String newPassword;
}
// Request para efetivar reset (token + nova senha)
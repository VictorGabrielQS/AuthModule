package VictorCode.AuthModule.dto;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String email;
}
// Request para solicitar reset
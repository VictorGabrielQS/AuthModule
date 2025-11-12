package VictorCode.AuthModule.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String token;
    private String newPassword;

}

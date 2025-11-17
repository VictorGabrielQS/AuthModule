package VictorCode.AuthModule.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@PasswordMatches
public class RegisterRequest {

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    private String email;

    @NotBlank(message = "Numero de telefone obrigatório")
    private String phoneNumber;

    @NotBlank(message = "A senha é obrigatória")
    private String password;

    @NotBlank(message = "A confirmação de senha é obrigatória")
    private String confirmPassword;

    @NotBlank(message = "O tipo de conta é obrigatório")
    private String accountType; // "personal" ou "aluno"

    private boolean receiveUpdates; // true se o usuário marcar o checkbox



}

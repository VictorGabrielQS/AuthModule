package VictorCode.AuthModule.controller;


import VictorCode.AuthModule.dto.EmailRequest;
import VictorCode.AuthModule.dto.ResetPasswordRequest;
import VictorCode.AuthModule.repository.PasswordResetTokenRepository;
import VictorCode.AuthModule.repository.UserRepository;
import VictorCode.AuthModule.service.PasswordResetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Reset de Senha", description = "Endpoints de reset de senha de login do usuário")
public class PasswordResetController {


    private final UserRepository userRepository;
    private final PasswordResetService passwordResetService;


    @PostMapping("/request")
    public ResponseEntity<String> solicitarReset(@RequestBody EmailRequest emailRequest){
        passwordResetService.createPasswordResetTokenAndSendEmail(emailRequest.getEmail());
        return ResponseEntity.ok("Email de redefinição de senha enviado para: " + emailRequest.getEmail());
    }


    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetSenha(@RequestBody ResetPasswordRequest resetPasswordRequest){
         passwordResetService.resetPassword(resetPasswordRequest.getToken() , resetPasswordRequest.getNewPassword());
        return ResponseEntity.ok("Senha Trocada com Sucesso!");
    }



}

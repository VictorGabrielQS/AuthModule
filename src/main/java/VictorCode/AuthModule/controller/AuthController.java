package VictorCode.AuthModule.controller;


import VictorCode.AuthModule.dto.LoginRequest;
import VictorCode.AuthModule.dto.RegisterRequest;
import VictorCode.AuthModule.model.User;
import VictorCode.AuthModule.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints de login e registro de usuários")
public class AuthController {

    private  final AuthService authService;


    // Controller - Criar Novo Usuario
    @Operation(summary = "Registrar um novo usuário")
    @PostMapping("/register")
    public ResponseEntity<User> criarUsuario(@RequestBody  RegisterRequest registerRequest){
        User user = authService.register(registerRequest);
        return ResponseEntity.ok(user);
    }


    // Controller - Logar Usuario
    @Operation(summary = "Fazer login e obter token JWT")
    @PostMapping("/login")
    public ResponseEntity<String> loginUsuario(@RequestBody LoginRequest loginRequest){
        String token = authService.login(loginRequest);
        return ResponseEntity.ok(token);
    }

}

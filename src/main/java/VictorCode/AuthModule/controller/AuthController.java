package VictorCode.AuthModule.controller;


import VictorCode.AuthModule.dto.LoginRequest;
import VictorCode.AuthModule.dto.RegisterRequest;
import VictorCode.AuthModule.model.User;
import VictorCode.AuthModule.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private  final AuthService authService;


    // Controller - Criar Novo Usuario
    @PostMapping("/registerUser")
    public ResponseEntity<User> criarUsuario(@RequestBody  RegisterRequest registerRequest){
        User user = authService.register(registerRequest);
        return ResponseEntity.ok(user);
    }


    // Controller - Logar Usuario
    @GetMapping("/loginUser")
    public ResponseEntity<String> loginUsuario(@RequestBody LoginRequest loginRequest){
        String token = authService.login(loginRequest);
        return ResponseEntity.ok(token);
    }

}

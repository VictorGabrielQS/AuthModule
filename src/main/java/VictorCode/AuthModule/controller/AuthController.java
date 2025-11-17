package VictorCode.AuthModule.controller;


import VictorCode.AuthModule.dto.LoginRequest;
import VictorCode.AuthModule.dto.RegisterRequest;
import VictorCode.AuthModule.model.User;
import VictorCode.AuthModule.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<String> loginUsuario(@RequestBody LoginRequest loginRequest , @RequestParam(defaultValue = "false") boolean rememberMe, HttpServletResponse response){

        String token = authService.login(loginRequest);

        // Cria o cookie HttpOnly
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true); // não acessível pelo JS
        cookie.setSecure(false); // coloque true se estiver usando HTTPS
        cookie.setPath("/"); // disponível para toda a aplicação

        // Define o tempo de expiração dependendo do checkbox
        if (rememberMe) {
            cookie.setMaxAge(7 * 24 * 60 * 60); // 7 dias
        } else {
            cookie.setMaxAge(-1); // cookie de sessão (expira ao fechar o navegador)
        }

        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Fazer logout e remover cookie de acesso")
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // remove o cookie
        response.addCookie(cookie);
        return ResponseEntity.ok("Logout realizado com sucesso");
    }


}

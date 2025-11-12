package VictorCode.AuthModule.controller;

import VictorCode.AuthModule.dto.RegisterRequest;
import VictorCode.AuthModule.model.User;
import VictorCode.AuthModule.service.AuthService;
import VictorCode.AuthModule.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Controle Usuários", description = "Endpoints de controle sobre os usuário do sistema")
public class UserController {

    private final UserService userService;


    @GetMapping("/listAllUser")
    public ResponseEntity<List<User>> listarTodosUsuarios(){
        return ResponseEntity.ok(userService.findAll());
    }


    @GetMapping("user")
    public  ResponseEntity<User> listarUsuarioId(@RequestParam Long id){
        User user = userService.findByUserId(id);
        return ResponseEntity.ok(user);
    }


    @PostMapping("/createUser")
    public ResponseEntity<User> criarUsuario(@RequestBody User user){
        return ResponseEntity.ok(userService.createUser(user));
    }


    @PutMapping("/updateUser")
    public  ResponseEntity<User> atualizarUsuario(@RequestParam Long id, @RequestBody User user){
        userService.updateUser(id , user);
        return ResponseEntity.ok(user);
    }

    @Transactional
    @DeleteMapping("deleteUser")
    public ResponseEntity<Void> deletarUsuario(@RequestParam Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

}

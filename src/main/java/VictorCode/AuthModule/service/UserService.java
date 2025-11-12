package VictorCode.AuthModule.service;


import VictorCode.AuthModule.model.Role;
import VictorCode.AuthModule.model.User;
import VictorCode.AuthModule.repository.RoleRepository;
import VictorCode.AuthModule.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository , PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    // 🧾 Listar Todos os usuarios do Sistema
    public List<User> findAll(){
        return userRepository.findAll();
    }


    // 🔍 Buscar Usuario por Id
    public User findByUserId(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }


    // ➕ criar usuario
    public User createUser(User user){

        if (user.getEmail() == null || user.getEmail().isEmpty()){
            throw new IllegalArgumentException("Email é obrigatório!");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()){
            throw new IllegalArgumentException("Senha é obrigatória!");
        }

        if (userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("Email já está em uso!");
        }

        // Criptografa a senha antes de salvar
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role 'ROLE_USER' não encontrada"));
        user.setRole(userRole);

        return userRepository.save(user);


    }


    // ✏️ Atualizar dados
    public  User updateUser(Long id, User userData){
        
        User user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setName(userData.getName());
        user.setEmail(userData.getEmail());
        user.setRole(userData.getRole());

        return userRepository.save(userData);
    }

    // ❌ Deletar
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));


        // (Opcional) Evita que admin se auto-delete
        if ("ADMIN".equals(user.getRole())) {
            throw new RuntimeException("Não é permitido deletar um administrador");
        }

        userRepository.delete(user);
    }
}

// Gerencia o CRUD de usuários (buscar, criar, editar, deletar, listar).
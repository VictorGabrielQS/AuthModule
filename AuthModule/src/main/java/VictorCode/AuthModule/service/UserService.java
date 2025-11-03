package VictorCode.AuthModule.service;


import VictorCode.AuthModule.model.User;
import VictorCode.AuthModule.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository , PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    }

}

// Gerencia o CRUD de usuários (buscar, criar, editar, deletar, listar).
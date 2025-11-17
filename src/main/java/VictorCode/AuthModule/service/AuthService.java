package VictorCode.AuthModule.service;

import VictorCode.AuthModule.dto.LoginRequest;
import VictorCode.AuthModule.dto.RegisterRequest;
import VictorCode.AuthModule.model.Role;
import VictorCode.AuthModule.model.User;
import VictorCode.AuthModule.repository.RoleRepository;
import VictorCode.AuthModule.repository.UserRepository;
import VictorCode.AuthModule.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository , JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 🧾 Registrar novo usuário
    public User register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email já cadastrado!");
        }




        // Determina a role com base no tipo de conta
        String roleName = registerRequest.getAccountType().equals("personal") ? "ROLE_PERSONAL" : "ROLE_ALUNO";

        Role userRole = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role '" + roleName + "' não encontrada"));

        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(userRole);
        user.setReceiveUpdates(registerRequest.isReceiveUpdates());

        return userRepository.save(user);
    }


    // 🔐 Login e geração do token JWT
    public String login(LoginRequest loginRequest){
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new RuntimeException("Senha incorreta");
        }

        // Gera o token JWT
        return  jwtTokenProvider.generateToken(user.getName());
    }


}

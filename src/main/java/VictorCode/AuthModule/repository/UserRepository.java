package VictorCode.AuthModule.repository;

import VictorCode.AuthModule.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 🔎 Verificar se email já está cadastrado
    boolean existsByEmail(String email);

    // 🔍 Método customizado para buscar usuário por e-mail
    Optional<User> findByEmail(String email);
}

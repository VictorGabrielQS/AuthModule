package VictorCode.AuthModule.service;

import VictorCode.AuthModule.model.PasswordResetToken;
import VictorCode.AuthModule.model.User;
import VictorCode.AuthModule.repository.PasswordResetTokenRepository;
import VictorCode.AuthModule.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;


    @Value("${app.reset-password.token-expiration-minutes:60}")
    private long tokenExpirationMinutes;

    @Value("${app.frontend.reset-url}")
    private String frontendResetUrl; // ex: https://seusite.com/reset-password?token=

    @Value("${spring.mail.username}")
    private String fromEmail;


    public PasswordResetService(UserRepository userRepository,
                                PasswordResetTokenRepository tokenRepository,
                                JavaMailSender mailSender,
                                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }



    // Cria token e envia e-mail com link.
    @Transactional
    public void createPasswordResetTokenAndSendEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Nenhum usuário encontrado para o e-mail informado"));


        // Gera token único
        String token = UUID.randomUUID().toString();
        Instant expiry = Instant.now().plus(tokenExpirationMinutes, ChronoUnit.MINUTES);

        PasswordResetToken passwordResetToken = new PasswordResetToken(token, expiry, user);
        tokenRepository.save(passwordResetToken);


        // Monta link de reset (o frontend deve pegar token e abrir tela para nova senha)
        String resetLink = frontendResetUrl + token;

        // Envia e-mail simples (pode ser HTML)
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject("Redefinição de senha - Seu App");
        message.setText("Olá " + user.getName()+ ",\n\n" +
                "Recebemos uma solicitação para redefinir sua senha. Clique no link abaixo para criar uma nova senha:\n\n" +
                resetLink + "\n\n" +
                "Se você não solicitou, ignore este e-mail.\n\n" +
                "Atenciosamente,\nEquipe");
        mailSender.send(message);
    }



     // Valida token e altera a senha do usuário.
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = tokenRepository.findByToken(token)
                .orElseThrow(()-> new RuntimeException("Token inválido"));

        if (passwordResetToken.isExpired()){
            throw new RuntimeException("Token Expirado");
        }

        if (passwordResetToken.isUsed()){
            throw new RuntimeException("Token já Utilizado");
        }


        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // marca token como usado (invalida reuso)
        passwordResetToken.setUsed(true);
        tokenRepository.save(passwordResetToken);


    }



     // Limpeza de tokens antigos (pode ser chamado por um scheduler)
    public void purgeExpiredTokens() {
        tokenRepository.deleteAllByExpiryDateBefore(Instant.now());
    }


}

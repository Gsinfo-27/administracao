package admin.gestao.api.emaill.controller;


import admin.gestao.api.emaill.services.EmailService;
import admin.gestao.api.emaill.services.SmsService;
import admin.gestao.dto.EmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.SecureRandom;

@RestController
@RequestMapping("/api/sms")
public class EmailController {

    @Autowired
    private SmsService smsService;
    @Autowired
    private EmailService emailService;

    private static final SecureRandom random = new SecureRandom();

    // Endpoint para enviar código de verificação
    @PostMapping("/send")
    public String sendVerificationCode(@RequestParam("phone") String phone) {
        // Gera código de 6 dígitos
        String code = String.format("%06d", random.nextInt(1000000));

        // Monta a mensagem
        String message = "Seu código de verificação é: " + code;

        // Envia SMS
        smsService.sendSms(phone, message);

        // Retorna o código (apenas para teste; em produção, guarde no DB ou cache)
        return "Código enviado para " + phone;
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailDto dto) {
        try {
            emailService.sendEmail(dto);
            return ResponseEntity.ok("Email enviado com sucesso!");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao enviar email: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Dados inválidos: " + e.getMessage());
        }
    }

}

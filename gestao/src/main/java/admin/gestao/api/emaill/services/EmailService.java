package admin.gestao.api.emaill.services;

import admin.gestao.dto.EmailDto;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${sendgrid.api-key}")
    private String apiKey;

    public void sendEmail(EmailDto dto) throws IOException {
        if (dto.getSendTo() == null || dto.getSendTo().isEmpty()) {
            throw new IllegalArgumentException("O destinatário não pode estar vazio");
        }

        Email from = new Email("tridonjoseacacio@gmail.com");
        Email to = new Email(dto.getSendTo());

        // Construindo HTML diretamente
        String htmlContent = "<!DOCTYPE html>"
                + "<html lang=\"pt-BR\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<title>Ative Sua Conta</title>"
                + "<style>"
                + "body { font-family: 'Verdana', sans-serif; background-color: #f9f5f2; color: #516775; margin: 0; padding: 0; }"
                + ".wrapper { width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 40px; box-sizing: border-box; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); text-align: center; }"
                + "h1 { color: #993300; font-size: 28px; margin-bottom: 20px; }"
                + "p { font-size: 16px; margin-bottom: 20px; }"
                + ".activation-code { display: inline-block; font-size: 24px; font-weight: bold; color: #993300; background-color: #f1f1f1; padding: 15px 25px; border-radius: 8px; letter-spacing: 2px; margin-bottom: 30px; }"
                + "@media screen and (max-width: 480px) { .wrapper { padding: 20px; } h1 { font-size: 24px; } p { font-size: 14px; } .activation-code { font-size: 20px; padding: 10px 20px; } }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class=\"wrapper\">"
                + "<h1>Ative Sua Conta</h1>"
                + "<p>Bem-vindo! Para completar seu cadastro, use o código de ativação abaixo:</p>"
                + "<div class=\"activation-code\">" + dto.getActivationCode() + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        // Texto plano
        String plainText = "Seu código de ativação é: " + dto.getActivationCode();

        Content contentPlain = new Content("text/plain", plainText);
        Content contentHtml = new Content("text/html", htmlContent);

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject(dto.getSubject());

        Personalization personalization = new Personalization();
        personalization.addTo(to);
        mail.addPersonalization(personalization);

        mail.addContent(contentPlain);
        mail.addContent(contentHtml);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Body: " + response.getBody());
            System.out.println("Headers: " + response.getHeaders());

            if (response.getStatusCode() >= 400) {
                throw new RuntimeException("Erro ao enviar email: " + response.getBody());
            }

        } catch (IOException ex) {
            throw ex;
        }
    }
}

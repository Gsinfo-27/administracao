package admin.gestao.dto;

public class EmailDto {

    private String sendTo;         // Destinatário
    private String subject;        // Assunto do e-mail
    private String activationCode; // Código de ativação

    public EmailDto() {}

    public EmailDto(String sendTo, String subject, String activationCode) {
        this.sendTo = sendTo;
        this.subject = subject;
        this.activationCode = activationCode;
    }

    public String getSendTo() { return sendTo; }
    public void setSendTo(String sendTo) { this.sendTo = sendTo; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getActivationCode() { return activationCode; }
    public void setActivationCode(String activationCode) { this.activationCode = activationCode; }
}

package admin.gestao.api.emaill.model;

import admin.gestao.dto.EmailStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailModel {
    @NotBlank
    private String cliente;
    @NotBlank
    @jakarta.validation.constraints.Email
    private String emailFrom;
    @NotBlank
    private String emailTo;
    @NotBlank
    private String subject;
    @NotBlank
    private String message;
    private EmailStatus status;
    private boolean html;
}

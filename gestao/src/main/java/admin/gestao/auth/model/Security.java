package admin.gestao.auth.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "security")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Security {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String Cliente;
    private UUID idUser;
    @Column(unique = true, nullable = false)
    private String keygen;
    private String endpoint;

    @Column(nullable = false)
    private Boolean status = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateTime = LocalDateTime.now();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCliente() {
        return Cliente;
    }

    public void setCliente(String cliente) {
        Cliente = cliente;
    }

    public UUID getIdUser() {
        return idUser;
    }

    public void setIdUser(UUID idUser) {
        this.idUser = idUser;
    }

    public String getKeygen() {
        return keygen;
    }

    public void setKeygen(String keygen) {
        this.keygen = keygen;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @PrePersist
    public void prePersist() {
        if (dateTime == null) {
            dateTime = LocalDateTime.now();
        }
    }
}

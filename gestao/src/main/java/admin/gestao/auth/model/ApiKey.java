package admin.gestao.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "api_keys", indexes = {
        @Index(name = "idx_api_keys_keygen", columnList = "keygen")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiKey {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String keygen;

    @Column(nullable = false)
    private String cliente;

    @Column(nullable = false)
    private String endpoint;

    @Column(nullable = false)
    private boolean status = true;

    @Column(nullable = false)
    private LocalDateTime dateTime;




}

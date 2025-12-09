package admin.gestao.auth.repository;

import admin.gestao.auth.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InfoRepository extends JpaRepository<Person, UUID> {
}

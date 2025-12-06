package admin.gestao.repository;

import admin.gestao.auth.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InfoRepository extends JpaRepository<Person, UUID> {
}

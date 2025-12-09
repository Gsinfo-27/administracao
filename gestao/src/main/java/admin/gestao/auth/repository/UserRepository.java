package admin.gestao.auth.repository;

import admin.gestao.auth.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, UUID> {
    @Query("SELECT u FROM Users u WHERE u.userName = :name AND u.deleted=false")
    Optional<Users> findByUsers(@Param("name") String name);
    Users findByUserName(String username);

    @Query("SELECT u FROM Users u WHERE u.deleted=false")
    List<Users> findByAllUsers();
}

package uz.uat.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.uat.backend.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    Optional<User> findByPinfl(String pinfl);

    Optional<User> findById(String id);
}
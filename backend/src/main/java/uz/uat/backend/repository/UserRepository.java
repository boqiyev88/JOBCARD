package uz.uat.backend.repository;

import feign.Param;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.User;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("select u from users u where u.isDeleted=0 and u.username= :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("select u from users u where u.isDeleted=0 and u.pinfl= :pinfl")
    Optional<User> findByPinfl(@Param("pinfl") String pinfl);

    @Query("select u from users u where u.isDeleted=0 and u.id= :id")
    Optional<User> findById(@NotNull @Param("id") String id);

}

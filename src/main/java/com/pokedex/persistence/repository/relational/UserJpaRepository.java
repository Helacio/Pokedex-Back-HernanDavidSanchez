package com.pokedex.persistence.repository.relational;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pokedex.persistence.entity.relational.UserEntity;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}

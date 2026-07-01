package com.pokedex.persistence.repository.relational;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pokedex.persistence.entity.relational.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
}

package com.pokedex.persistence.entity.relational;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "users") // "user" es palabra reservada en PostgreSQL, se usa "users"
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA necesita constructor sin args
@AllArgsConstructor(access = AccessLevel.PRIVATE)   // Solo Builder puede llamar al constructor completo
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password; // se guarda ya encriptada (BCrypt)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Roles del sistema según sección 1.2 del plan de trabajo
    public enum Role {
        GUEST, TRAINER, ADMIN
    }
}
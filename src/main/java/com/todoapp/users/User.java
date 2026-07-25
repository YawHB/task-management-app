package com.todoapp.users;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name="users")
 class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "First name must not be blank")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    private String lastName;

    @Email(message = "Email must be a valid address")
    @NotBlank(message = "Email must not be blank")
    @Column(unique = true)
    private String email;

    private String username;
    private String profilePicture;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted;
    //private String keyCloakId;

    public User(UUID id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}

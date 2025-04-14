package single.project.e_commerce.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import single.project.e_commerce.utils.annotations.EnumPattern;
import single.project.e_commerce.utils.enums.Gender;
import single.project.e_commerce.utils.enums.Status;

import java.util.Set;

@Setter
@Getter
@Builder
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotNull(message = "Username must not be null!")
    @Size(min = 4, message = "Username size must be greater than or equal 4!")
    @Column(name = "username", unique = true)
    private String username;

    @NotNull(message = "Password must not be null!")
    @Size(min = 4, message = "Password size must be greater than or equal 4!")
    @Column(name = "password", unique = true)
    private String password;

    @NotBlank(message = "Email must not be null!")
    @Email(message = "Input is not email type!")
    @Column(name = "email")
    private String email;


    @EnumPattern(name = "gender", regexp = "MALE|FEMALE")
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;


    @EnumPattern(name = "status", regexp = "ACTIVE|INACTIVE|LOCKED")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToMany
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;


    @NotNull(message = "Address must be required!")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;

    @OneToMany(mappedBy = "user")
    private Set<Review> reviews;

    @OneToMany(mappedBy = "user")
    private Set<Payment> payments;
}

package single.project.e_commerce.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "tokens")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Token extends AbstractEntity {
    @Id
    private String jti;
    private String username;
}

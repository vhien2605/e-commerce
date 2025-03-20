package single.project.e_commerce.models;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Setter
@Getter
@MappedSuperclass
public abstract class AbstractEntity {
    @Column(name = "create_at")
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    private Date create_at;

    @Column(name = "update_at")
    @UpdateTimestamp
    @Temporal(TemporalType.DATE)
    private Date update_at;
}


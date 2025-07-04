package single.project.e_commerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import single.project.e_commerce.models.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}

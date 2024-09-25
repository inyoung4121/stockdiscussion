package in.userservice.user.repository;

import in.userservice.user.domain.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Long> {
    Certification findByEmail(String email);
}

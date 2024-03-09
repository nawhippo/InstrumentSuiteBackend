package InstrumentSuite.InstrumentSuite.PracticePlan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PracticePlanRepository extends JpaRepository<PracticePlan, Long> {
    List<PracticePlan> findByUserId(Long userId);
}
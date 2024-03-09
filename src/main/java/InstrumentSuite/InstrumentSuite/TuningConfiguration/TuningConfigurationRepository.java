package InstrumentSuite.InstrumentSuite.TuningConfiguration;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TuningConfigurationRepository extends JpaRepository<TuningConfiguration, Long> {
    List<TuningConfiguration> findByInstrumentId(Long instrumentId);
}
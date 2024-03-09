package InstrumentSuite.InstrumentSuite.Chord;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChordRepository extends JpaRepository<Chord, Long> {
    List<Chord> findByTuningConfigurationId(Long tuningConfigurationId);
}

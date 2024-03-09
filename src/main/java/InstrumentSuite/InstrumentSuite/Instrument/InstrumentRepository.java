package InstrumentSuite.InstrumentSuite.Instrument;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
    @Query("SELECT i FROM Instrument i WHERE i.id = :id")
    Optional<Instrument> findInstrumentById(Long id);

    List<Instrument> findAll();
}
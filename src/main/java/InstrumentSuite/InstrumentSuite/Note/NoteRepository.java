package InstrumentSuite.InstrumentSuite.Note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Optional<Note> findByName(String name);
    Note findByMidiNote(int midiNote);
    List<Note> findByNameAndMidiNote(String name, int midiNote);
}
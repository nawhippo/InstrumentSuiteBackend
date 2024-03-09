package InstrumentSuite.InstrumentSuite.Chord;
import InstrumentSuite.InstrumentSuite.Note.Note;
import InstrumentSuite.InstrumentSuite.Note.NoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChordService {

    private final ChordRepository chordRepository;
    private final NoteRepository noteRepository;

    @Autowired
    public ChordService(ChordRepository chordRepository, NoteRepository noteRepository) {

        this.chordRepository = chordRepository;
        this.noteRepository = noteRepository;
    }

    public Chord saveChord(Chord chord) {
        List<Note> existingNotes = new ArrayList<>();
        for (Note note : chord.getNotes()) {
            Note existingNote = noteRepository.findByName(note.getName())
                    .orElseThrow(() -> new EntityNotFoundException("Note not found with name: " + note.getName()));
            existingNotes.add(existingNote);
        }
        chord.setNotes(existingNotes);
        return chordRepository.save(chord);
    }


    public Chord updateChord(Chord chord) {
        return chordRepository.save(chord);
    }

    public Optional<Chord> getChordById(Long chordId) {
        return chordRepository.findById(chordId);
    }

    public void deleteChord(Chord chord) {
        chordRepository.delete(chord);
    }

    public List<Chord> getChordsByTuningId(Long instrumentId) {
        return chordRepository.findByTuningConfigurationId(instrumentId);
    }
}
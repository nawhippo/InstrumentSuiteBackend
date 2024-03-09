package InstrumentSuite.InstrumentSuite.TuningConfiguration;

import InstrumentSuite.InstrumentSuite.Note.Note;
import InstrumentSuite.InstrumentSuite.Note.NoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TuningConfigurationService {

    @Autowired
    private final TuningConfigurationRepository tuningConfigurationRepository;

    @Autowired
    private NoteRepository noteRepository;

    public TuningConfigurationService(TuningConfigurationRepository tuningConfigurationRepository, NoteRepository noteRepository) {
        this.tuningConfigurationRepository = tuningConfigurationRepository;
        this.noteRepository = noteRepository;
    }

    public List<TuningConfiguration> findAllTuningConfigurations() {
        return tuningConfigurationRepository.findAll();
    }

    public Optional<TuningConfiguration> findTuningConfigurationById(Long id) {
        return tuningConfigurationRepository.findById(id);
    }

    public TuningConfiguration saveTuningConfiguration(TuningConfiguration tuningConfiguration) {
        List<Note> existingNotes = new ArrayList<>();
        for (Note note : tuningConfiguration.getNotes()) {
            Note existingNote = noteRepository.findByName(note.getName())
                    .orElseThrow(() -> new EntityNotFoundException("Note not found with name: " + note.getName()));
            existingNotes.add(existingNote);
        }
        tuningConfiguration.setNotes(existingNotes);
        return tuningConfigurationRepository.save(tuningConfiguration);
    }
    public TuningConfiguration updateTuningConfiguration(Long id, TuningConfiguration tuningConfiguration) {
        if (tuningConfigurationRepository.existsById(id)) {
            tuningConfiguration.setId(id);
            return tuningConfigurationRepository.save(tuningConfiguration);
        }
        return null;
    }

    public void deleteTuningConfiguration(Long id) {
        tuningConfigurationRepository.deleteById(id);
    }

    public List<TuningConfiguration> findTuningConfigurationsByInstrumentId(Long instrumentId) {
        return tuningConfigurationRepository.findByInstrumentId(instrumentId);
    }
}
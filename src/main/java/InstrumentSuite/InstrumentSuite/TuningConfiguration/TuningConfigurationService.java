package InstrumentSuite.InstrumentSuite.TuningConfiguration;

import InstrumentSuite.InstrumentSuite.Instrument.Instrument;
import InstrumentSuite.InstrumentSuite.Instrument.InstrumentService;
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
    private final InstrumentService instrumentService;

    @Autowired
    private NoteRepository noteRepository;

    public TuningConfigurationService(TuningConfigurationRepository tuningConfigurationRepository, NoteRepository noteRepository, InstrumentService instrumentService) {
        this.tuningConfigurationRepository = tuningConfigurationRepository;
        this.noteRepository = noteRepository;
        this.instrumentService = instrumentService;
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

        Instrument instrument = instrumentService.getInstrumentById(tuningConfiguration.getInstrumentId())
                .orElseThrow(() -> new EntityNotFoundException("Instrument not found with id: " + tuningConfiguration.getInstrumentId()));

        Note[][] fretboard = instrumentService.generateFretboard(instrument, tuningConfiguration);
        tuningConfiguration.setFretboard(fretboard);
        return tuningConfigurationRepository.save(tuningConfiguration);
    }

    public void deleteTuningConfiguration(Long id) {
        tuningConfigurationRepository.deleteById(id);
    }

    public List<TuningConfiguration> findTuningConfigurationsByInstrumentId(Long instrumentId) {
        return tuningConfigurationRepository.findByInstrumentId(instrumentId);
    }

    public TuningConfiguration updateTuningConfiguration(Long id, TuningConfiguration updatedTuningConfiguration) {
        TuningConfiguration existingTuningConfiguration = tuningConfigurationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TuningConfiguration not found with id: " + id));
        existingTuningConfiguration.setName(updatedTuningConfiguration.getName());
        boolean notesUpdated = !existingTuningConfiguration.getNotes().equals(updatedTuningConfiguration.getNotes());
        if (notesUpdated) {
            List<Note> existingNotes = new ArrayList<>();
            for (Note note : updatedTuningConfiguration.getNotes()) {
                Note existingNote = noteRepository.findByName(note.getName())
                        .orElseThrow(() -> new EntityNotFoundException("Note not found with name: " + note.getName()));
                existingNotes.add(existingNote);
            }
            existingTuningConfiguration.setNotes(existingNotes);
            Instrument instrument = instrumentService.getInstrumentById(existingTuningConfiguration.getInstrumentId())
                    .orElseThrow(() -> new EntityNotFoundException("Instrument not found with id: " + existingTuningConfiguration.getInstrumentId()));

            Note[][] fretboard = instrumentService.generateFretboard(instrument, existingTuningConfiguration);
            existingTuningConfiguration.setFretboard(fretboard);
        }
        return tuningConfigurationRepository.save(existingTuningConfiguration);
    }
}
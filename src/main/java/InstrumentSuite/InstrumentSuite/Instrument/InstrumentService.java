package InstrumentSuite.InstrumentSuite.Instrument;

import InstrumentSuite.InstrumentSuite.Chord.Chord;
import InstrumentSuite.InstrumentSuite.Note.Note;
import InstrumentSuite.InstrumentSuite.Note.NoteRepository;
import InstrumentSuite.InstrumentSuite.TuningConfiguration.TuningConfiguration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InstrumentService {
    NoteRepository noteRepository;
    InstrumentRepository instrumentRepository;

    InstrumentService(NoteRepository noteRepository, InstrumentRepository instrumentRepository){

        this.noteRepository = noteRepository;
        this.instrumentRepository = instrumentRepository;
    }

    //this will be cached on the frontend
    public Note[][] generateFretboard(Instrument instrument, TuningConfiguration tuning) {
        System.out.println("Generating fretboard...");
        Note[][] fretboard = new Note[instrument.getNumberOfStrings()][instrument.getNumberOfFrets() + 1];

        if(tuning.getNotes().size() != instrument.getNumberOfStrings()) {
            System.out.println(tuning.getNotes().size());
            System.out.println(instrument.getNumberOfStrings());
            System.out.println("Mismatch in the number of strings and tuning notes.");
        }

        for (int stringIndex = 0; stringIndex < instrument.getNumberOfStrings(); stringIndex++) {
            Note openNote = tuning.getNotes().get(stringIndex);
            if (openNote == null) {
                System.out.println("Open note for string index " + stringIndex + " is null.");
                continue;
            }
            for (int fret = 0; fret <= instrument.getNumberOfFrets(); fret++) {
                int midiNote = openNote.getMidiNote() + fret;
                Note note = noteRepository.findByMidiNote(midiNote);
                if (note == null) {
                    System.out.println("No note found for MIDI note " + midiNote);
                } else {
                    System.out.println("Found note for MIDI note " + midiNote + ": " + note.getName());
                }
                fretboard[stringIndex][fret] = note;
            }
        }

        return fretboard;
    }



    public List<String> tabChord(Instrument instrument, TuningConfiguration tuning, Chord chord) {
        Note[][] fretboard = generateFretboard(instrument, tuning);
        List<String> tab = new ArrayList<>();

        for (Note chordNote : chord.getNotes()) {
            boolean noteFound = false;
            for (int stringIndex = 0; stringIndex < fretboard.length && !noteFound; stringIndex++) {
                for (int fretIndex = 0; fretIndex < fretboard[stringIndex].length; fretIndex++) {
                    if (fretboard[stringIndex][fretIndex].equals(chordNote)) {
                        tab.add("String " + (stringIndex + 1) + ", Fret " + fretIndex);
                        noteFound = true;
                        break;
                    }
                }
            }
            if (!noteFound) {
                tab.add("Note " + chordNote.getName() + " not found on fretboard.");
            }
        }

        return tab;
    }

    public Optional<Instrument> getInstrumentById(Long id) {
        return instrumentRepository.findInstrumentById(id);
    }

    public List<Instrument> getAllInstruments() {
        return instrumentRepository.findAll();
    }
}

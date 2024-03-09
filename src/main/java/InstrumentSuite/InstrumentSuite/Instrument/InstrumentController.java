package InstrumentSuite.InstrumentSuite.Instrument;

import InstrumentSuite.InstrumentSuite.Chord.Chord;
import InstrumentSuite.InstrumentSuite.Note.Note;
import InstrumentSuite.InstrumentSuite.TuningConfiguration.TuningConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/instruments")
public class InstrumentController {

    private final InstrumentService instrumentService;

    @Autowired
    public InstrumentController(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    // Endpoint to generate fretboard
    @PostMapping("/fretboard")
    public ResponseEntity<Note[][]> generateFretboard(@RequestBody InstrumentRequest request) {
        Instrument instrument = request.getInstrument();
        TuningConfiguration tuning = request.getTuning();
        Note[][] fretboard = instrumentService.generateFretboard(instrument, tuning);
        return ResponseEntity.ok(fretboard);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Instrument> getInstrumentById(@PathVariable Long id) {
        Optional<Instrument> instrument = instrumentService.getInstrumentById(id);
        if (instrument.isPresent()) {
            return ResponseEntity.ok(instrument.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getAllInstruments")
    public ResponseEntity<List<Instrument>> getAllInstruments() {
        List<Instrument> instruments = instrumentService.getAllInstruments();
        return ResponseEntity.ok(instruments);
    }

    @PostMapping("/tabChord")
    public ResponseEntity<List<String>> tabChord(@RequestBody InstrumentRequest request, @RequestBody Chord chord) {
        Instrument instrument = request.getInstrument();
        TuningConfiguration tuning = request.getTuning();
        List<String> tab = instrumentService.tabChord(instrument, tuning, chord);
        return ResponseEntity.ok(tab);
    }
}
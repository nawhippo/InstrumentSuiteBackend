package InstrumentSuite.InstrumentSuite.Chord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chords")
public class ChordController {

    private final ChordService chordService;

    @Autowired
    public ChordController(ChordService chordService) {
        this.chordService = chordService;
    }

    @PostMapping
    public ResponseEntity<Chord> createChord(@RequestBody Chord chord) {
        Chord createdChord = chordService.saveChord(chord);
        return ResponseEntity.ok(createdChord);
    }

    @PutMapping
    public ResponseEntity<Chord> updateChord(@RequestBody Chord chord) {
        if (chord.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Chord updatedChord = chordService.updateChord(chord);
        return ResponseEntity.ok(updatedChord);
    }
    @DeleteMapping
    public ResponseEntity<Void> deleteChord(@RequestBody Chord chord) {
        if (chord.getId() == null || !chordService.getChordById(chord.getId()).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        chordService.deleteChord(chord);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chord> getChordById(@PathVariable Long id) {
        Optional<Chord> chord = chordService.getChordById(id);
        return chord.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/byTuning/{tuningId}")
    public ResponseEntity<List<Chord>> getChordsByTuning(@PathVariable Long tuningId) {
        List<Chord> chords = chordService.getChordsByTuningId(tuningId);
        return ResponseEntity.ok(chords);
    }
}
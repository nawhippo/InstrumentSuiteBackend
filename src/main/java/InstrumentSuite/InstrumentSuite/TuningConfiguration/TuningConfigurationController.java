package InstrumentSuite.InstrumentSuite.TuningConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tuningConfigurations")
public class TuningConfigurationController {

    private final TuningConfigurationService tuningConfigurationService;

    @Autowired
    public TuningConfigurationController(TuningConfigurationService tuningConfigurationService) {
        this.tuningConfigurationService = tuningConfigurationService;
    }

    @GetMapping
    public ResponseEntity<List<TuningConfiguration>> getAllTuningConfigurations() {
        return ResponseEntity.ok(tuningConfigurationService.findAllTuningConfigurations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TuningConfiguration> getTuningConfigurationById(@PathVariable Long id) {
        return tuningConfigurationService.findTuningConfigurationById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TuningConfiguration> createTuningConfiguration(@RequestBody TuningConfiguration tuningConfiguration) {
        return ResponseEntity.ok(tuningConfigurationService.saveTuningConfiguration(tuningConfiguration));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TuningConfiguration> updateTuningConfiguration(@PathVariable Long id, @RequestBody TuningConfiguration tuningConfiguration) {
        TuningConfiguration updatedTuningConfiguration = tuningConfigurationService.updateTuningConfiguration(id, tuningConfiguration);
        if (updatedTuningConfiguration != null) {
            return ResponseEntity.ok(updatedTuningConfiguration);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTuningConfiguration(@PathVariable Long id) {
        tuningConfigurationService.deleteTuningConfiguration(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/byInstrument/{instrumentId}")
    public ResponseEntity<List<TuningConfiguration>> getTuningConfigurationsByInstrumentId(@PathVariable Long instrumentId) {
        return ResponseEntity.ok(tuningConfigurationService.findTuningConfigurationsByInstrumentId(instrumentId));
    }
}
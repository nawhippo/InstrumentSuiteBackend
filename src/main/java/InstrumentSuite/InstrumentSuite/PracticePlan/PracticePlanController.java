package InstrumentSuite.InstrumentSuite.PracticePlan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/practicePlans")
public class PracticePlanController {

    @Autowired
    private PracticePlanService practicePlanService;

    @GetMapping
    public ResponseEntity<List<PracticePlan>> getAll() {
        return ResponseEntity.ok(practicePlanService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PracticePlan> getById(@PathVariable Long id) {
        return practicePlanService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PracticePlan> create(@RequestBody PracticePlan practicePlan) {
        return ResponseEntity.ok(practicePlanService.save(practicePlan));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PracticePlan> update(@PathVariable Long id, @RequestBody PracticePlan practicePlan) {
        practicePlan.setId(id);
        return ResponseEntity.ok(practicePlanService.update(practicePlan));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        practicePlanService.delete(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/byUser")
    public ResponseEntity<List<PracticePlan>> getAllByUserId(@RequestParam Long userId) {
        List<PracticePlan> practicePlans = practicePlanService.findAllByUserId(userId);
        if (practicePlans.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(practicePlans);
    }
}
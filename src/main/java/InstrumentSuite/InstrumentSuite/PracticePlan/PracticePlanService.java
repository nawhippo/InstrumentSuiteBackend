package InstrumentSuite.InstrumentSuite.PracticePlan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PracticePlanService {

    @Autowired
    private PracticePlanRepository repository;

    public List<PracticePlan> findAll() {
        return repository.findAll();
    }

    public Optional<PracticePlan> findById(Long id) {
        return repository.findById(id);
    }

    public PracticePlan save(PracticePlan practicePlan) {
        return repository.save(practicePlan);
    }

    public PracticePlan update(PracticePlan practicePlan) {
        return repository.save(practicePlan);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<PracticePlan> findAllByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

}
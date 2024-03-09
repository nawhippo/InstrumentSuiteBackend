package InstrumentSuite.InstrumentSuite;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SequenceAdjusterRunner implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public SequenceAdjusterRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        // Assuming you've determined the next ID should start from 113
        jdbcTemplate.execute("ALTER SEQUENCE note_id_seq RESTART WITH 113");
    }
}
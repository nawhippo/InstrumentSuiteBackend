package InstrumentSuite.InstrumentSuite.Chord;

import InstrumentSuite.InstrumentSuite.Note.Note;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chord {
    private String name;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_id_gen")
    @SequenceGenerator(name = "global_id_gen", sequenceName = "global_id_sequence", allocationSize = 1)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "chord_id")
    private List<Note> notes;

    @ElementCollection
    private List<Integer> fretPositions;

    @JoinColumn(name = "tuning_configuration_id")
    private Long tuningConfigurationId;
}

package InstrumentSuite.InstrumentSuite.TuningConfiguration;

import InstrumentSuite.InstrumentSuite.Note.Note;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TuningConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_id_gen")
    @SequenceGenerator(name = "global_id_gen", sequenceName = "global_id_sequence", allocationSize = 1)
    Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "tuning_configuration_notes",
            joinColumns = @JoinColumn(name = "tuning_configuration_id"),
            inverseJoinColumns = @JoinColumn(name = "note_id")
    )
    private List<Note> notes;

    @Column
    Long instrumentId;

    @Column
    private String name;
}
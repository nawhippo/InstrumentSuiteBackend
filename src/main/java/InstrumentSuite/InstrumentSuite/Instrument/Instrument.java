package InstrumentSuite.InstrumentSuite.Instrument;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Instrument")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Instrument {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_id_gen")
    @SequenceGenerator(name = "global_id_gen", sequenceName = "global_id_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "number_of_strings")
    private int numberOfStrings;

    @Column(name = "number_of_frets")
    private int numberOfFrets;

    @Column(name = "name")
    private String name;
}
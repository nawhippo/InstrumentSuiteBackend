package InstrumentSuite.InstrumentSuite.Note;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Note")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //A1, C5 etc.
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "midi_note", unique = true, nullable = false)
    private int midiNote;

    public Note(String name, int midiNote) {
        this.name = name;
        this.midiNote = midiNote;
    }
}
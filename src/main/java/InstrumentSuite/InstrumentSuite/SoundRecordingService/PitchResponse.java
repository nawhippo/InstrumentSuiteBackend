package InstrumentSuite.InstrumentSuite.SoundRecordingService;

import InstrumentSuite.InstrumentSuite.Note.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PitchResponse {
    private Note note;
    private Float difference;

    public PitchResponse(String name) {
        this.note = new Note(name, 0);
    }

    public PitchResponse(String name, float difference) {
        this.note = new Note(name, 0);
        this.difference = difference;
    }

    public PitchResponse(String name, float difference, int midiNote) {
        this.note = new Note(name, midiNote);
        this.difference = difference;
    }


    public PitchResponse(Note note, float difference) {
        this.note = note;
        this.difference = difference;
    }
}

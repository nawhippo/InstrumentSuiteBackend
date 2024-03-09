package InstrumentSuite.InstrumentSuite.SoundRecordingService;

public class MidiNoteConverter {

    public static int noteNameToMidi(String note) {
        int octaveStartPos = findOctaveStartPos(note);
        if (octaveStartPos == -1) {
            return -1;
        }

        String noteName = note.substring(0, octaveStartPos);
        int octave = Integer.parseInt(note.substring(octaveStartPos));

        int midiNote = noteNameToMidiNoteBase(noteName) + (octave + 1) * 12;
        return midiNote;
    }

    private static int findOctaveStartPos(String note) {
        for (int i = 0; i < note.length(); i++) {
            if (Character.isDigit(note.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    private static int noteNameToMidiNoteBase(String noteName) {
        return switch (noteName) {
            case "C" -> 0;
            case "C#" -> 1;
            case "D" -> 2;
            case "D#" -> 3;
            case "E" -> 4;
            case "F" -> 5;
            case "F#" -> 6;
            case "G" -> 7;
            case "G#" -> 8;
            case "A" -> 9;
            case "A#" -> 10;
            case "B" -> 11;
            default -> -1;
        };
    }
}

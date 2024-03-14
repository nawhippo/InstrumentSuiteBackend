package InstrumentSuite.InstrumentSuite.SoundRecordingService;
import InstrumentSuite.InstrumentSuite.Note.Note;
import InstrumentSuite.InstrumentSuite.Note.NoteRepository;
import InstrumentSuite.InstrumentSuite.TuningConfiguration.TuningConfigurationRepository;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;
import jakarta.persistence.EntityNotFoundException;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

@Service
@NoArgsConstructor
public class InstrumentTuningService {
    private static final Logger logger = LoggerFactory.getLogger(InstrumentTuningService.class);
    TuningConfigurationRepository tuningConfigurationRepository;
    NoteRepository noteRepository;

    @Autowired
    public InstrumentTuningService(TuningConfigurationRepository tuningConfigurationRepository, NoteRepository noteRepository) {
        this.tuningConfigurationRepository = tuningConfigurationRepository;
        this.noteRepository = noteRepository;
    }

    public PitchResponse estimateNote(float pitch) {
        logger.info("Estimating note for pitch: {}", pitch);
        if (pitch <= 0) {
            logger.warn("Pitch is less than or equal to 0. Returning N/A.");
            return new PitchResponse(new Note("N/A", 0), 0f);
        }

        int midiNote = (int) Math.round(12 * log2(pitch / 440.0) + 69);
        double frequencyOfNote = 440 * Math.pow(2, (midiNote - 69) / 12.0);
        float differenceInCents = (float) (1200 * log2(pitch / frequencyOfNote));
        int noteIndex = midiNote % 12;
        String noteName = noteName(noteIndex);

        int calculatedMidiNote = MidiNoteConverter.noteNameToMidi(noteName + ((midiNote / 12) - 1));
        if (calculatedMidiNote != midiNote) {
            logger.error("Mismatch in calculated MIDI note numbers for note: {} with MIDI: {}", noteName, midiNote);
        }

        logger.info("Estimated note: {}, MIDI: {}, Difference in cents: {}", noteName, midiNote, differenceInCents);
        return new PitchResponse(new Note(noteName, midiNote), differenceInCents);
    }
    private double log2(double a) {
        return Math.log(a) / Math.log(2.0);
    }

    private String noteName(int noteIndex) {
        return switch (noteIndex) {
            case 0 -> "C";
            case 1 -> "C#";
            case 2 -> "D";
            case 3 -> "D#";
            case 4 -> "E";
            case 5 -> "F";
            case 6 -> "F#";
            case 7 -> "G";
            case 8 -> "G#";
            case 9 -> "A";
            case 10 -> "A#";
            case 11 -> "B";
            default -> "?";
        };
    }

    public float pitchReturner(AudioInputStream audioInputStream) {
        logger.info("Starting pitch returner");
        try {
            AudioFormat format = audioInputStream.getFormat();
            logger.info("Format of audio input stream: {}", format.toString());
            int bufferSize = 4096;
            int overlap = 2048;

            AudioDispatcher dispatcher = new AudioDispatcher(new JVMAudioInputStream(audioInputStream), bufferSize, overlap);
            final float[] pitch = {0f};

            dispatcher.addAudioProcessor(new PitchProcessor(
                    PitchEstimationAlgorithm.FFT_YIN,
                    format.getSampleRate(),
                    bufferSize,
                    (PitchDetectionResult result, AudioEvent audioEvent) -> {
                        if (result.getPitch() != -1) {
                            pitch[0] = result.getPitch();
                            logger.info("Detected pitch: {}", pitch[0]);
                            dispatcher.stop();
                        }
                    }
            ));

            new Thread(dispatcher, "Audio Dispatcher").start();
            dispatcher.run();
            logger.info("Returning pitch: {}", pitch[0]);
            return pitch[0];
        } catch (Exception e) {
            logger.error("Error occurred in pitchReturner: ", e);
            return 0f;
        }
    }
    public PitchResponse getNoteFromAudioStream(AudioInputStream audioInputStream) throws UnsupportedAudioFileException, IOException {
        logger.info("Getting note from the audio stream");
        float pitch = pitchReturner(audioInputStream);
        logger.info("Detected pitch from audio stream: {}", pitch);
        return estimateNote(pitch);
    }

    public PitchResponse getDifferenceGivenNote(AudioInputStream audioInputStream, String expectedNoteName) {
        logger.info("Getting difference for given note: {}", expectedNoteName);
        float detectedPitch = pitchReturner(audioInputStream);
        Note note = noteRepository.findByName(expectedNoteName)
                .orElseThrow(() -> new EntityNotFoundException("Note not found with name: " + expectedNoteName));
        float expectedFrequency = (float) (440 * Math.pow(2, (note.getMidiNote() - 69) / 12.0));
        float differenceInHz = detectedPitch - expectedFrequency;

        logger.info("Detected pitch: {}, Expected frequency: {}, Difference in Hz: {}", detectedPitch, expectedFrequency, differenceInHz);
        return new PitchResponse(note, differenceInHz);
    }
}
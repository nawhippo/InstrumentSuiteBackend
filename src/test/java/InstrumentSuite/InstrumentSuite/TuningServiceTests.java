package InstrumentSuite.InstrumentSuite;
import InstrumentSuite.InstrumentSuite.SoundRecordingService.InstrumentTuningService;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TuningServiceTests {

    public static AudioInputStream getAudioInputStreamForFrequency(int frequency) throws UnsupportedAudioFileException, IOException {
        float sampleRate = 44100;
        float duration = 3.0f;
        double[] buffer = new double[(int) (duration * sampleRate)];
        byte[] audioData = new byte[buffer.length * 2];

        //generate sine wave data
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = Math.sin(2 * Math.PI * i * frequency / sampleRate);
        }


        int bufferIndex = 0;
        for (int i = 0; i < buffer.length; i++) {
            short val = (short) (buffer[i] * Short.MAX_VALUE);
            audioData[bufferIndex++] = (byte) (val & 0x00ff);
            audioData[bufferIndex++] = (byte) ((val & 0xff00) >>> 8);
        }

        // Create an AudioInputStream from the byte array
        AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
        ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
        AudioInputStream audioInputStream = new AudioInputStream(bais, format, audioData.length / format.getFrameSize());

        return audioInputStream;
    }

    @Test
    public void testPitchDetection() throws UnsupportedAudioFileException, IOException {
        // This is conceptual; actual implementation depends on your setup
        final float hz = 1.0f;
        AudioInputStream audioInputStream = getAudioInputStreamForFrequency(440);
        InstrumentTuningService service = new InstrumentTuningService();
        float detectedPitch = service.pitchReturner(audioInputStream);
        System.out.println(detectedPitch);
        assertTrue(Math.abs(detectedPitch - 440) < hz);
    }

    @Test
    public void testMultiplePitchesDetection() throws UnsupportedAudioFileException, IOException {
        InstrumentTuningService service = new InstrumentTuningService();
        float[] frequencies = {440, 880, 220};
        for (float frequency : frequencies) {
            float detectedPitch = service.pitchReturner(TuningServiceTests.getAudioInputStreamForFrequency((int) frequency));
            assertEquals(frequency, detectedPitch, 1);
        }
    }


}
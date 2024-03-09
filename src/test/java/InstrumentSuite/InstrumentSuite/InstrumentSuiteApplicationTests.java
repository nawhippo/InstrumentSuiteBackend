//package InstrumentSuite.InstrumentSuite;
//
//import InstrumentSuite.InstrumentSuite.SoundRecordingService.FrequencyProcessingService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.sound.sampled.AudioFormat;
//import javax.sound.sampled.AudioInputStream;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//
//import static InstrumentSuite.InstrumentSuite.AudioTestUtils.generateSineWaveStream;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@SpringBootTest
//class InstrumentSuiteApplicationTests {
//
//	//error 1 assumpotion that the stream would provide all the necessary info all at once, miss calculated sample size
//	private FrequencyProcessingService frequencyProcessingService;
//	private AudioInputStream audioInputStream;
//
//	@BeforeEach
//	void setup() {
//		frequencyProcessingService = new FrequencyProcessingService();
//		audioInputStream = generateSineWaveStream(440, 44100, 16, 1, 3); // Example: 440 Hz, 44100 sample rate, 16-bit, mono, 3 seconds
//		AudioFormat standardFormat = new AudioFormat(
//				AudioFormat.Encoding.PCM_SIGNED,
//				44100.0F,
//				16,
//				1,
//				4,
//				44100.0F,
//				//TODO: perhaps double check this later
//				false
//		);
//
//	}
//
//	@Test
//	void testConvertBytesToFloats() throws IOException {
//		// Setup the audio format as before
//		AudioFormat standardFormat = new AudioFormat(
//				AudioFormat.Encoding.PCM_SIGNED,
//				44100.0F,
//				8,
//				1,
//				1,
//				44100.0F,
//				false
//		);
//
//		// Byte array with a single sample of value 1
//		byte[] testBytes = new byte[]{1};
//		ByteArrayInputStream bais = new ByteArrayInputStream(testBytes);
//		AudioInputStream audioInputStream = new AudioInputStream(bais, standardFormat, testBytes.length);
//
//		// Assuming your method returns a float array
//		FrequencyProcessingService frequencyProcessingService = new FrequencyProcessingService();
//		float[] result = frequencyProcessingService.convertBytesToFloats(audioInputStream, standardFormat);
//
//		// Assert that the result is as expected
//		assertNotNull(result, "The result should not be null.");
//		assertEquals(1, result.length, "The result should contain one float value.");
//		float expectedValue = 1.0f / 127.0f; // Normalizing 1 to the -1.0 to 1.0 range for 8-bit audio
//		assertEquals(expectedValue, result[0], 0.0001, "The converted float value does not match the expected normalized value.");
//	}
//	@Test
//	void testConvertBytesToFloatsWithMaxValue() throws IOException {
//		AudioFormat standardFormat = new AudioFormat(
//				AudioFormat.Encoding.PCM_SIGNED,
//				44100.0F, // Sample Rate
//				8,        // Sample Size in Bits
//				1,        // Channels
//				1,        // Frame Size
//				44100.0F, // Frame Rate
//				false     // Big Endian
//		);
//
//		byte[] testBytes = new byte[]{127};
//		ByteArrayInputStream bais = new ByteArrayInputStream(testBytes);
//		AudioInputStream audioInputStream = new AudioInputStream(bais, standardFormat, testBytes.length);
//
//
//		FrequencyProcessingService frequencyProcessingService = new FrequencyProcessingService();
//		float[] result = frequencyProcessingService.convertBytesToFloats(audioInputStream, standardFormat);
//
//
//		assertNotNull(result, "The result should not be null.");
//		assertEquals(1, result.length, "The result should contain one float value.");
//
//		assertEquals(1.0f, result[0], 0.01, "The converted float value for maximum 8-bit value does not match the expected value.");
//	}
//	@Test
//	void testConvertBytesToFloatsWithMinValue() throws IOException {
//		AudioFormat standardFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0F, 8, 1, 1, 44100.0F, false);
//		byte[] testBytes = new byte[]{-128};
//		ByteArrayInputStream bais = new ByteArrayInputStream(testBytes);
//		AudioInputStream audioInputStream = new AudioInputStream(bais, standardFormat, testBytes.length);
//
//		FrequencyProcessingService frequencyProcessingService = new FrequencyProcessingService();
//		float[] result = frequencyProcessingService.convertBytesToFloats(audioInputStream, standardFormat);
//
//		assertNotNull(result, "The result should not be null.");
//		assertEquals(1, result.length, "The result should contain one float value.");
//		assertEquals(-1.0f, result[0], 0.01, "The converted float value for minimum 8-bit value does not match the expected value.");
//	}
//
//	@Test
//	void testConvertBytesToFloatsWith16BitStereo() throws IOException {
//		// Define a 16-bit, stereo, PCM_SIGNED AudioFormat
//		AudioFormat stereoFormat = new AudioFormat(
//				AudioFormat.Encoding.PCM_SIGNED,
//				44100.0F, // Sample Rate
//				16,       // Sample Size in Bits
//				2,        // Channels
//				4,        // Frame Size (2 bytes per sample * 2 channels)
//				44100.0F, // Frame Rate
//				false     // Big Endian
//		);
//
//		// Create a byte array representing a short array with values (max value for one sample, min value for the other)
//		// To simulate stereo, we'll alternate between a high and a low value for left and right channels
//		byte[] testBytes = new byte[]{(byte)0xFF, (byte)0x7F, (byte)0x00, (byte)0x80}; // {32767, -32768} in 16-bit
//		ByteArrayInputStream bais = new ByteArrayInputStream(testBytes);
//		AudioInputStream audioInputStream = new AudioInputStream(bais, stereoFormat, 2);
//
//		FrequencyProcessingService frequencyProcessingService = new FrequencyProcessingService();
//		float[] result = frequencyProcessingService.convertBytesToFloats(audioInputStream, stereoFormat);
//
//		assertNotNull(result, "The result should not be null.");
//		assertEquals(2, result.length, "The result should contain two float values for the stereo format.");
//		// Assuming the conversion logic correctly handles 16-bit stereo, we should validate against expected float values.
//		// These expected values depend on how the conversion formula is adapted for 16-bit data.
//		// This is a placeholder assertion; the actual expected values need to be calculated based on the implementation details.
//		assertEquals(1.0f, result[0], 0.01, "The converted float value for the first channel does not match the expected value.");
//		assertEquals(-1.0f, result[1], 0.01, "The converted float value for the second channel does not match the expected value.");
//	}
//
//	@Test
//	void testConvertBytesToFloatsWithZeroValue() throws IOException {
//		AudioFormat standardFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0F, 8, 1, 1, 44100.0F, false);
//		byte[] testBytes = new byte[]{0};
//		ByteArrayInputStream bais = new ByteArrayInputStream(testBytes);
//		AudioInputStream audioInputStream = new AudioInputStream(bais, standardFormat, testBytes.length);
//
//		FrequencyProcessingService frequencyProcessingService = new FrequencyProcessingService();
//		float[] result = frequencyProcessingService.convertBytesToFloats(audioInputStream, standardFormat);
//
//		assertNotNull(result, "The result should not be null.");
//		assertEquals(1, result.length, "The result should contain one float value.");
//		assertEquals(0.0f, result[0], 0.01, "The converted float value for zero byte does not match the expected value.");
//	}
//	@Test
//	void testFrequency() throws IOException {
//		// Assuming processAudioInformation returns a frequency in Hz and your expected frequency is 440Hz
//		float calculatedFrequency = frequencyProcessingService.processAudioInformation(audioInputStream, audioInputStream.getFormat());
//		assertEquals(440, calculatedFrequency, "The calculated frequency should be close to 440 Hz");
//	}
//}
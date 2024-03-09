//package InstrumentSuite.InstrumentSuite;
//import javax.sound.sampled.*;
//import java.io.ByteArrayInputStream;
//
//public class AudioTestUtils {
//
//
//    public static AudioInputStream generateSineWaveStream(float frequency, int sampleRate, int sampleSizeInBits, int channels, long durationInSeconds) {
//        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, true, false);
//        int bytesPerSample = sampleSizeInBits / 8;
//        byte[] buffer = new byte[(int) (durationInSeconds * sampleRate * channels * bytesPerSample)];
//        double twoPiF = 2 * Math.PI * frequency;
//
//        for (int sample = 0; sample < buffer.length / (channels * bytesPerSample); sample++) {
//            double time = sample / (double) sampleRate;
//            double amplitude = Math.sin(twoPiF * time);
//            int value = (int) ((amplitude * Math.pow(2, sampleSizeInBits - 1)) - 1);
//
//            for (int channel = 0; channel < channels; channel++) {
//                int offset = (sample * channels * bytesPerSample) + (channel * bytesPerSample);
//                for (int byteIndex = 0; byteIndex < bytesPerSample; byteIndex++) {
//                    // Note: For multi-byte samples (like 16-bit), this assumes little-endian byte order
//                    buffer[offset + byteIndex] = (byte) ((value >>> (8 * byteIndex)) & 0xFF);
//                }
//            }
//        }
//
//        return new AudioInputStream(new ByteArrayInputStream(buffer), format, buffer.length / format.getFrameSize());
//    }
//}

package InstrumentSuite.InstrumentSuite.SoundRecordingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/api/record")
public class SoundRecordingController {

    @Autowired
    InstrumentTuningService tuningService;

    @Autowired
    public SoundRecordingController(InstrumentTuningService tuningService) {
        this.tuningService = tuningService;
    }

    @PostMapping(value = "/getNote", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PitchResponse> soundRecordingController(@RequestParam("audioData") MultipartFile audioData,
                                                                  @RequestParam(required = false) String note) {
        try {
            Path tempAudioPath = Files.createTempFile("audio", ".webm");
            audioData.transferTo(tempAudioPath.toFile());

            System.out.println("Converting audio file to wav");
            Path tempWavPath = Files.createTempFile("converted_audio", ".wav");
            ProcessBuilder processBuilder = new ProcessBuilder("ffmpeg", "-y", "-i", tempAudioPath.toString(), tempWavPath.toString());
            processBuilder.redirectErrorStream(true);
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            System.out.println("Starting processbuilder");
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("process builder line: " + line);
                }
            }
            boolean finished = process.waitFor(60, TimeUnit.SECONDS);
            if (!finished) {
                System.out.println("FFmpeg process timed out.");
                process.destroy();
                throw new RuntimeException("FFmpeg process timed out.");
            }
            int exitCode = process.exitValue();
            if (exitCode != 0) {
                System.out.println("YOWZAH, AUDIO CONVERSION PROBLEM, GOD DAMN IT");
                throw new RuntimeException("Failed to convert audio with FFmpeg.");
            }
            File wavFile = tempWavPath.toFile();
            System.out.println("wav " + wavFile);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile);
            PitchResponse pitchResponse;
            if(note != null) {
                pitchResponse = tuningService.getDifferenceGivenNote(audioInputStream, note);
            } else {
                pitchResponse = tuningService.getNoteFromAudioStream(audioInputStream);
            }
            pitchResponse.getNote().setMidiNote(MidiNoteConverter.noteNameToMidi(pitchResponse.getNote().getName()));

            Files.delete(tempAudioPath);
            Files.delete(tempWavPath);

            return ResponseEntity.ok().body(pitchResponse);
        } catch (UnsupportedAudioFileException | IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new PitchResponse("Error processing the audio file."));
        }
    }
    }


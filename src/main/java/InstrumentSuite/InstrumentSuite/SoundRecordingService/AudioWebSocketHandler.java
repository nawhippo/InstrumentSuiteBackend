package InstrumentSuite.InstrumentSuite.SoundRecordingService;

import InstrumentSuite.InstrumentSuite.Note.Note;
import InstrumentSuite.InstrumentSuite.Note.NoteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class AudioWebSocketHandler implements WebSocketHandler {
    private final NoteRepository noteRepository;

    private final InstrumentTuningService tuningService;

    @Autowired
    public AudioWebSocketHandler(InstrumentTuningService tuningService, NoteRepository noteRepository) {
        this.tuningService = tuningService;
        this.noteRepository = noteRepository;
    }

    ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connection Established.");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

        try {
            String payload = message.getPayload().toString();
            System.out.println("Received payload: " + payload);
            ObjectNode node = objectMapper.readValue(payload, ObjectNode.class);
            String parsedNote = node.get("note").asText();
            String base64AudioData = node.get("audioData").asText();


            byte[] audioBytes = Base64.getDecoder().decode(base64AudioData);


            Path tempAudioPath = Files.createTempFile("audio", ".webm");
            Files.write(tempAudioPath, audioBytes);

            System.out.println("Converting audio file to wav");
            Path tempWavPath = Files.createTempFile("converted_audio", ".wav");
            ProcessBuilder processBuilder = new ProcessBuilder("ffmpeg", "-y", "-i", tempAudioPath.toString(), tempWavPath.toString());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Wait for the process to complete and check the result
            boolean finished = process.waitFor(60, TimeUnit.SECONDS);
            if (!finished) {
                throw new RuntimeException("FFmpeg process timed out.");
            }
            int exitCode = process.exitValue();
            if (exitCode != 0) {
                throw new RuntimeException("Failed to convert audio with FFmpeg.");
            }
//            Note note = noteRepository.findByName(parsedNote);
            File wavFile = tempWavPath.toFile();
            System.out.println("wav " + wavFile);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("process builder line: " + line);
                }
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile);
            PitchResponse pitchResponse;
            if (!parsedNote.isEmpty()) {
                pitchResponse = tuningService.getDifferenceGivenNote(audioInputStream, parsedNote);
            } else {
                pitchResponse = tuningService.getNoteFromAudioStream(audioInputStream);
            }
            pitchResponse.getNote().setMidiNote(MidiNoteConverter.noteNameToMidi(pitchResponse.getNote().getName()));
            String pitchResponseJson = objectMapper.writeValueAsString(pitchResponse);
            TextMessage responseMessage = new TextMessage(pitchResponseJson);
            session.sendMessage(responseMessage);
            Files.delete(tempAudioPath);
            Files.delete(tempWavPath);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = String.format("{\"error\": \"Error processing the audio file: %s\"}", e.getMessage());
            session.sendMessage(new TextMessage(errorMessage));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    //perhaps through exception
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("Connection Closed");
        //perhaps clean up files in storage?
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
        //presumably for corrupted/ incomplete messages
    }
}

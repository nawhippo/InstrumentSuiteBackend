package InstrumentSuite.InstrumentSuite.SoundRecordingService;
import InstrumentSuite.InstrumentSuite.Note.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final InstrumentTuningService tuningService;
    private final NoteRepository noteRepository;

    @Autowired
    public WebSocketConfig(InstrumentTuningService tuningService, NoteRepository noteRepository) {
        this.tuningService = tuningService;
        this.noteRepository = noteRepository;
    }


    @Bean
    public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(32768);
        container.setMaxBinaryMessageBufferSize(32768);
        return container;
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "/ws/audio")
                .setAllowedOrigins("http://localhost:3000", "http://localhost:8081");
//                .withSockJS()
//                .setStreamBytesLimit(15 * 1024)
//                .setHttpMessageCacheSize(15 * 1024)
//                .setDisconnectDelay(30 * 1000);
    }


    public WebSocketHandler myHandler() {
        return new AudioWebSocketHandler(tuningService, noteRepository);
    }
}

package InstrumentSuite.InstrumentSuite.Instrument;
import InstrumentSuite.InstrumentSuite.TuningConfiguration.TuningConfiguration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentRequest {
    private Instrument instrument;
    private TuningConfiguration tuning;

}
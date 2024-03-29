package ru.otus.dataprocessor;

import org.junit.jupiter.api.Test;
import ru.otus.model.Measurement;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ProcessorAggregatorTest {
    @Test
    void processTest() {
        List<Measurement> measurements = new ArrayList<>();
        measurements.add(new Measurement("val1", 10.0));
        measurements.add(new Measurement("val2", 20.0));
        measurements.add(new Measurement("val1", 5.0));
        measurements.add(new Measurement("val2", 15.0));
        measurements.add(new Measurement("val3", 1.0));

        ProcessorAggregator processor = new ProcessorAggregator();
        var groupedMeasurements = processor.process(measurements);

        assertThat(groupedMeasurements.get("val1")).isEqualTo(15.0);
        assertThat(groupedMeasurements.get("val2")).isEqualTo(35.0);
        assertThat(groupedMeasurements.get("val3")).isEqualTo(1.0);
    }
}

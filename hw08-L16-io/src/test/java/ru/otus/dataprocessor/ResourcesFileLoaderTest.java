package ru.otus.dataprocessor;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ResourcesFileLoaderTest {
    @Test
    void loadTest() throws IOException {
        File file = new File("build/resources/test/inputData.json");
        var loader = new ResourcesFileLoader(file.getPath());
        var loadedMeasurements = loader.load();
        assertThat(loadedMeasurements).hasSize(9);
        //for (Measurement measurement: loadedMeasurements){
        //    System.out.println(measurement.toString());
        //}
    }

}

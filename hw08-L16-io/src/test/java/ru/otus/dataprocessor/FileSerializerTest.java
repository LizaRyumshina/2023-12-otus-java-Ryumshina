package ru.otus.dataprocessor;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class FileSerializerTest {
    @Test
    void processingTest(@TempDir Path tempDir) throws IOException {
        var outputDataFileName = "outputData.json";
        var fullOutputFilePath = String.format("%s%s%s", tempDir, File.separator, outputDataFileName);
        var serializer = new FileSerializer(fullOutputFilePath);
        Map<String, Double> measurement = new TreeMap<>();
        measurement.put("val1", 1.0);
        serializer.serialize(measurement);
        var file = new File(fullOutputFilePath);
        assertThat(file.exists()).isTrue();
        var serializedOutput = Files.readString(Paths.get(fullOutputFilePath));
        // обратите внимание: важен порядок ключей
        AssertionsForClassTypes.assertThat(serializedOutput).isEqualTo("{\"val1\":1.0}");
    }
}

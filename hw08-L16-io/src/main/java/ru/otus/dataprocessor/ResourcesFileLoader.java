package ru.otus.dataprocessor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {
    private final String fileName;
    private final ObjectMapper mapper = JsonMapper.builder().build();

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        // читает файл, парсит и возвращает результат
        try {
            //System.out.println( mapper.readValue(new File(fileName), new TypeReference<List<Measurement>>() {}));
            return mapper.readValue(new File(fileName), new TypeReference<List<Measurement>>() {});
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}

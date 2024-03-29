package ru.otus.dataprocessor;

import java.util.*;

import ru.otus.model.Measurement;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        // группирует выходящий список по name, при этом суммирует поля value
        List<Measurement> sortedList = new ArrayList<>(data);
        sortedList.sort(Comparator.comparing(Measurement::name));

        Map<String, Double> result = new TreeMap<>();
        for (Measurement m : sortedList) {
            result.put(m.name(), result.getOrDefault(m.name(), 0.0) + m.value());
        }
        return result;
    }
}

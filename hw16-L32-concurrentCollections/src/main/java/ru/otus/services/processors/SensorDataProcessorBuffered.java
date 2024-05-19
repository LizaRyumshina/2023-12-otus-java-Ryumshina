package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

// Этот класс нужно реализовать
@SuppressWarnings({"java:S1068", "java:S125"})
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private ConcurrentSkipListSet<SensorData> dataBuffer;
    private List<SensorData> bufferedData;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        dataBuffer = new ConcurrentSkipListSet<>(Comparator.comparing(SensorData::getMeasurementTime));
    }

    @Override
    public void process(SensorData data) {
        try {
            if (data != null && !data.getValue().isNaN()) {
                dataBuffer.add(data);
                if (dataBuffer.size() >= bufferSize) {
                    flush();
                }
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера в process", e);
        }
    }

    public synchronized void flush() {
        if (dataBuffer.isEmpty()) {
            return;
        }
        try {
            bufferedData = new ArrayList<>();
            int cnt = Math.min(dataBuffer.size(), bufferSize); // запуск может быть из onProcessingEnd, т.е. не полный набор элементов
            for (int i = 0; i < cnt; i++) {
                bufferedData.add(dataBuffer.pollFirst()); // сразу удаляет элемент из очереди
            }
            writer.writeBufferedData(bufferedData);
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}

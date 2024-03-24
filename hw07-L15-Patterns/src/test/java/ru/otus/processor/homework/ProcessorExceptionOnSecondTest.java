package ru.otus.processor.homework;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProcessorExceptionOnSecondTest {
    @Test
    void exceptionOnSecondTest() {
        DateTimeProvider fakeDateTimeProvider = () -> LocalDateTime.now().withSecond(2);
        Processor processor = new ProcessorExceptionOnSecond(fakeDateTimeProvider);

        Message message = new Message.Builder(1L).field1("field1").build();

        assertThrows(IllegalStateException.class, () -> processor.process(message));
    }

    @Test
    void noExceptionOnSecondTest() {
        DateTimeProvider fakeDateTimeProvider = () -> LocalDateTime.now().withSecond(3);
        Processor processor = new ProcessorExceptionOnSecond(fakeDateTimeProvider);

        Message message = new Message.Builder(1L).field1("field1").build();

        assertDoesNotThrow(() -> {
            processor.process(message);
        });
    }
}

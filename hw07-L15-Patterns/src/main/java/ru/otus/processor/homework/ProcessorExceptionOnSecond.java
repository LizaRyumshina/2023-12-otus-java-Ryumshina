package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorExceptionOnSecond implements Processor {
    private final DateTimeProvider dateTimeProvider;

    public ProcessorExceptionOnSecond(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        //System.out.println(dateTimeProvider.getDate().getSecond());
        if (dateTimeProvider.getDate().getSecond() % 2 == 0) {
            throw new IllegalStateException("Exception on the even second");
        }
        return message.toBuilder().build();
    }
}

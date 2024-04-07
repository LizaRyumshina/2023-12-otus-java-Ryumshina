package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EntityClassMetaDataImpl implements EntityClassMetaData {
    private Class<?> clazz;
    private Constructor constructor;
    private Field idField;
    private List<Field> fields;

    public EntityClassMetaDataImpl(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor getConstructor() {
        if (constructor == null) {
            try {
                constructor = clazz.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return constructor;
    }

    @Override
    public Field getIdField() {
        if (idField == null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    idField = field;
                    break;
                }
            }
        }
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        if (fields == null) {
            fields = List.of(clazz.getDeclaredFields());
        }
        return fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return getAllFields().stream().filter(field -> !Objects.equals(field, getIdField())).toList();
    }
}

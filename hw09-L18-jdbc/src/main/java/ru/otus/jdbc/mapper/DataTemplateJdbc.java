package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.demo.DbServiceDemo;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {
    private static final Logger log = LoggerFactory.getLogger(DataTemplateJdbc.class);

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), resultSet -> {
            try {
                if (resultSet.next()) {
                    return getResultFields(resultSet);
                }
            } catch (SQLException e) {
                log.error("Error: findById id =" + id);
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    private T getResultFields(ResultSet rs) {
        T obj;
        try {
            obj = entityClassMetaData.getConstructor().newInstance();
            Map<String, Field> fields = entityClassMetaData.getAllFields().stream().collect(Collectors.toMap(Field::getName, Function.identity()));

            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                String columnName = rs.getMetaData().getColumnName(i);
                Field field = fields.get(columnName);

                setField(field, obj, rs.getObject(i));
            }
        } catch (Exception e) {
            log.error("Error: getResultFields");
            throw new RuntimeException(e);
        }
        return obj;
    }
    private void setField(Field field, Object obj, Object value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            log.error("Error: setField");
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<T> findAll(Connection connection) {
        List<T> objs = null;
        try {
            dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), Collections.emptyList(), resultSet -> {
                try {
                    List<T> result = new ArrayList<>();
                    while (resultSet.next()) {
                        result.add(getResultFields(resultSet));
                    }
                    return result;
                } catch (SQLException e) {
                    log.error("Error: findAll (get values) ");
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            log.error("Error: findAll (execute select) ");
            throw new RuntimeException(e);
        }
        return objs;
    }

    @Override
    public long insert(Connection connection, T client) {
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), getValuesOfFields(client, false));
    }

    private List<Object> getValuesOfFields(T client, Boolean isAddIdField) {
        List<Object> values = new ArrayList<>();
        for (Field field : entityClassMetaData.getFieldsWithoutId()) {
            values.add(getField(field, client));
        }
        if (isAddIdField) {
            Field field = entityClassMetaData.getIdField();
            values.add(getField(field, client));
        }
        return values;
    }
    private Object getField(Field field, T client) {
        try {
            field.setAccessible(true);
            return field.get(client);
        } catch (IllegalAccessException e) {
            log.error("Error: getField");
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update(Connection connection, T client) {
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), getValuesOfFields(client, true));
    }
}

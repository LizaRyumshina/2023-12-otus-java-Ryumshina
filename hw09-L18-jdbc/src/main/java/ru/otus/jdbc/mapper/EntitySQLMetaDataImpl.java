package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return getSelect(false);
    }

    @Override
    public String getSelectByIdSql() {
        return getSelect(true);
    }

    private String getSelect(boolean isFilterById) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ").append("* ");
        sb.append("from ").append(entityClassMetaData.getName()).append(" ");
        if (isFilterById) {
            sb.append("where  ").append(entityClassMetaData.getIdField().getName()).append(" = ? ");
        }
        return sb.toString();
    }

    @Override
    public String getInsertSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(entityClassMetaData.getName()).append(" ");
        sb.append("(").append(separatedBy(entityClassMetaData.getFieldsWithoutId(), ",")).append(") ");
        sb.append("values (").append(prepareInputParams(entityClassMetaData.getFieldsWithoutId().size())).append(") ");
        return sb.toString();
    }

    @Override
    public String getUpdateSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("update ").append(entityClassMetaData.getName()).append(" ");
        sb.append("set ").append(prepareInputParams(entityClassMetaData.getFieldsWithoutId()));
        sb.append("where  ").append(entityClassMetaData.getIdField().getName()).append(" = ? ");
        return sb.toString();
    }

    private String prepareInputParams(List<Field> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).getName()).append(" = ?, ");
        }
        sb.deleteCharAt(sb.length() - 2);
        return sb.toString();
    }

    private String prepareInputParams(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append("?, ");
        }
        sb.deleteCharAt(sb.length() - 2);
        return sb.toString();
    }

    private String separatedBy(List<Field> fields, String separator) {
        StringBuilder sb = new StringBuilder();
        for (Field field : fields) {
            sb.append(field.getName()).append(separator);
        }
        sb.deleteCharAt(sb.length() - separator.length());
        return sb.toString();
    }
}

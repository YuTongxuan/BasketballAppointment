package cumt.innovative.training.project.basketballappointment.utils.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import cumt.innovative.training.project.basketballappointment.logger.Logger;
import cumt.innovative.training.project.basketballappointment.utils.DatabaseUtil;
import cumt.innovative.training.project.basketballappointment.utils.ExceptionUtil;
import cumt.innovative.training.project.basketballappointment.utils.annotation.Ignore;
import cumt.innovative.training.project.basketballappointment.utils.annotation.ReadOnly;

import java.lang.reflect.Field;
import java.util.Map;

public class TableUpdateHelper {
    public static String updateStatement(Object object) {
        String tableName = TableCreateHelper.getTableName(object.getClass());
        Map<Field, Object> insertItems = TableInsertHelper.getSqlItemMap(object);
        Field primaryKeyField = TableQueryHelper.getPrimaryKeyField(object.getClass());
        StringBuffer buf = new StringBuffer("UPDATE " + tableName + " SET ");
        boolean first = true;
        for (Field keyField : insertItems.keySet()) {
            if (keyField.getName().equals(primaryKeyField.getName())) {
                continue;
            }
            if(keyField.getAnnotation(ReadOnly.class) != null) {
                continue;
            }
            String value = TableInsertHelper.toSqlItemString(insertItems.get(keyField));
            if (!first) {
                buf.append(", ");
            }
            buf.append(keyField.getName() + "=" + value);
            if (first) {
                first = false;
            }
        }
        buf.append(" WHERE ");
        String primaryValue = TableInsertHelper.toSqlItemString(insertItems.get(primaryKeyField));
        if (primaryValue == null) {
            throw new RuntimeException("Primary key \"" + primaryKeyField.getName() + "\" has null value when deleting it from table \"" + tableName + "\"");
        }
        buf.append(primaryKeyField.getName() + "=" + primaryValue);

        return buf.toString();
    }

    public static <T> String updateStatement(String jsonString, Class<T> cls) {
        ObjectMapper mapper = new ObjectMapper();
        Map jsonObject = null;
        try {
            jsonObject = mapper.readValue(jsonString, Map.class);
        } catch (Exception e) {
            throw new RuntimeException(ExceptionUtil.getSimpleMessage(e));
        }

        String tableName = TableCreateHelper.getTableName(cls);
        Field primaryKeyField = TableQueryHelper.getPrimaryKeyField(cls);
        if(!jsonObject.keySet().contains(primaryKeyField.getName())) {
            throw new RuntimeException("Update error for lack of primary key \"" + primaryKeyField.getName() + "\"");
        }
        StringBuffer buf = new StringBuffer("UPDATE " + tableName + " SET ");
        boolean first = true;
        for (Field field : cls.getDeclaredFields()) {
            if (field.getAnnotation(Ignore.class) != null) {
                continue;
            }
            if(!jsonObject.keySet().contains(field.getName())) {
                continue;
            }
            if(field.getName().equals(primaryKeyField.getName())) {
                continue;
            }
            String value = TableInsertHelper.toSqlItemString(jsonObject.get(field.getName()));
            if (!first) {
                buf.append(", ");
            }
            buf.append(field.getName() + "=" + value);
            if (first) {
                first = false;
            }
        }
        buf.append(" WHERE ");
        String primaryValue = TableInsertHelper.toSqlItemString(jsonObject.get(primaryKeyField.getName()));
        if (primaryValue == null) {
            throw new RuntimeException("Primary key \"" + primaryKeyField.getName() + "\" has null value when deleting it from table \"" + tableName + "\"");
        }
        buf.append(primaryKeyField.getName() + "=" + primaryValue);

        return buf.toString();
    }

    public static int update(Object object) {
        String statement = updateStatement(object);
        Logger.log(new Throwable(), Logger.INFO, statement, true);
        return DatabaseUtil.executeUpdate(statement);
    }

    public static <T> int update(String jsonString, Class<T> cls) {
        String statement = updateStatement(jsonString, cls);
        Logger.log(new Throwable(), Logger.INFO, statement, true);
        return DatabaseUtil.executeUpdate(statement);
    }
}

package cumt.innovative.training.project.basketballappointment.utils.db;

import cumt.innovative.training.project.basketballappointment.logger.Logger;
import cumt.innovative.training.project.basketballappointment.utils.DatabaseUtil;
import cumt.innovative.training.project.basketballappointment.utils.ExceptionUtil;
import cumt.innovative.training.project.basketballappointment.utils.annotation.Ignore;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class TableInsertHelper {

    public static String toSqlItemString(Object item) {
        if (item instanceof String) {
            return "\'" + item + "\'";
        } else {
            return item.toString();
        }
    }

    public static Map<Field, Object> getSqlItemMap(Object obj) {
        Class<?> cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        Map<Field, Object> insertItems = new HashMap<>();
        for (Field field : fields) {
            if(field.getAnnotation(Ignore.class) != null) {
                continue;
            }
            String fieldName = field.getName();
            Object value = ReflectionHelper.getFieldValue(field, cls, obj);
            insertItems.put(field, value);
        }
        return insertItems;
    }

    public static String getInsertStatement(Object obj) {
        Map<Field, Object> insertItems = getSqlItemMap(obj);
        String tableName = TableCreateHelper.getTableName(obj.getClass());
        Field primaryKeyField = TableQueryHelper.getPrimaryKeyField(obj.getClass());
        StringBuffer keys = new StringBuffer();
        StringBuffer values = new StringBuffer();
        boolean first = true;
        for (Field keyField : insertItems.keySet()) {
            if (keyField.getName().equals(primaryKeyField.getName())) {
                continue;
            }
            Object value = insertItems.get(keyField);
            if (!first) {
                keys.append(", ");
                values.append(", ");
            }
            keys.append(keyField.getName());
            values.append(toSqlItemString(value));
            if (first) {
                first = false;
            }
        }
        String sqlCommand = "INSERT INTO " + tableName + " (" + keys + ") VALUES (" + values + ");";
        return sqlCommand;
    }

    public static int insert(Object obj) {
        String sqlCommand = getInsertStatement(obj);
        Logger.log(new Throwable(), Logger.INFO, sqlCommand, true);
        return DatabaseUtil.executeUpdate(sqlCommand);
    }
}

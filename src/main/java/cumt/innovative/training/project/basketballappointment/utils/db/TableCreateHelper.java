package cumt.innovative.training.project.basketballappointment.utils.db;

import cumt.innovative.training.project.basketballappointment.logger.Logger;
import cumt.innovative.training.project.basketballappointment.utils.DatabaseUtil;
import cumt.innovative.training.project.basketballappointment.utils.annotation.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class TableCreateHelper {

    public static Map<Class<?>, String> typeMapper;

    static {
        typeMapper = new HashMap<>();
        typeMapper.put(int.class, "integer");
        typeMapper.put(Integer.class, "integer");
        typeMapper.put(String.class, "text");
    }

    public static <T> String getTableName(Class<T> cls) {
        TableName tableName = cls.getAnnotation(TableName.class);
        if (tableName == null || tableName.value().equals("")) {
            throw new RuntimeException("Cannot find the table name related to class \"" + cls.getSimpleName() + "\"");
        }
        return tableName.value();
    }

    public static <T> String getCreateStatement(Class<T> cls) {
        StringBuffer buf = new StringBuffer("CREATE TABLE " + getTableName(cls) + " ( ");
        boolean first = true;
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(Ignore.class) != null) {
                continue;
            }
            Class<?> type = field.getType();
            if (!typeMapper.containsKey(type)) {
                throw new RuntimeException("Type of \"" + type.getSimpleName() + "\" is not defined in typeMapper");
            }
            String sqlTypeName = typeMapper.get(type);

            boolean isAutoIncrement = field.getAnnotation(AutoIncrement.class) != null;
            boolean isPrimaryKey = isAutoIncrement || (field.getAnnotation(PrimaryKey.class) != null);
            boolean nullable = !isPrimaryKey && (field.getAnnotation(Nullable.class) != null);
            String item = field.getName() + " " + sqlTypeName +
                    (!nullable && !isPrimaryKey ? " not null" : "") +
                    (isPrimaryKey ? " primary key" : "") +
                    (isAutoIncrement ? " autoincrement" : "");
            if (first) {
                buf.append(item);
                first = false;
            } else {
                buf.append(", " + item);
            }
        }
        buf.append(" );");
        return buf.toString();
    }

    public static <T> void createTable(Class<T> cls) {
        String createStatement = getCreateStatement(cls);
        Logger.log(new Throwable(), Logger.INFO, createStatement, true);
        DatabaseUtil.executeUpdate(createStatement);
    }
}

package cumt.innovative.training.project.basketballappointment.utils.db;

import cumt.innovative.training.project.basketballappointment.logger.Logger;
import cumt.innovative.training.project.basketballappointment.utils.DatabaseUtil;

import java.lang.reflect.Field;

public class TableDeleteHelper {
    public static String getDeleteStatement(Object object) {
        String tableName = TableCreateHelper.getTableName(object.getClass());
        Field primaryKeyField = TableQueryHelper.getPrimaryKeyField(object.getClass());
        String primaryValue = TableInsertHelper.toSqlItemString(TableInsertHelper.getSqlItemMap(object).get(primaryKeyField));
        if (primaryValue == null) {
            throw new RuntimeException("Primary key \"" + primaryKeyField.getName() + "\" has null value when deleting it from table \"" + tableName + "\"");
        }
        return "DELETE FROM " + tableName + " WHERE " + primaryKeyField.getName() + "=" + primaryValue;
    }

    public static <T> String getDeleteStatement(Object primaryKey, Class<T> cls) {
        String tableName = TableCreateHelper.getTableName(cls);
        Field primaryKeyField = TableQueryHelper.getPrimaryKeyField(cls);
        return "DELETE FROM " + tableName + " WHERE " + primaryKeyField.getName() + "=" + TableInsertHelper.toSqlItemString(primaryKey);
    }

    public static int delete(Object object) {
        String statement = getDeleteStatement(object);
        Logger.log(new Throwable(), Logger.INFO, statement, true);
        return DatabaseUtil.executeUpdate(statement);
    }

    public static <T> int delete(Object primaryKey, Class<T> cls) {
        String statement = getDeleteStatement(primaryKey, cls);
        Logger.log(new Throwable(), Logger.INFO, statement, true);
        return DatabaseUtil.executeUpdate(statement);
    }
}

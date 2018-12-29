package cumt.innovative.training.project.basketballappointment.utils.db;

import cumt.innovative.training.project.basketballappointment.logger.Logger;
import cumt.innovative.training.project.basketballappointment.utils.DatabaseUtil;
import cumt.innovative.training.project.basketballappointment.utils.ExceptionUtil;
import cumt.innovative.training.project.basketballappointment.utils.annotation.AutoIncrement;
import cumt.innovative.training.project.basketballappointment.utils.annotation.Ignore;
import cumt.innovative.training.project.basketballappointment.utils.annotation.PrimaryKey;
import cumt.innovative.training.project.basketballappointment.utils.interfaces.UtilExecutable;

import java.lang.reflect.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TableQueryHelper {

    public static Map<Class<?>, UtilExecutable> executableMapper;

    static {
        executableMapper = new HashMap<>();
        executableMapper.put(String.class, new UtilExecutable() {
            public Object execute(ResultSet resultSet, String columnName) {
                try {
                    return resultSet.getString(columnName);
                } catch (SQLException e) {
                    throw new RuntimeException(ExceptionUtil.getSimpleMessage(e));
                }
            }
        });
        executableMapper.put(int.class, new UtilExecutable() {
            public Object execute(ResultSet resultSet, String columnName) {
                try {
                    return resultSet.getInt(columnName);
                } catch (SQLException e) {
                    throw new RuntimeException(ExceptionUtil.getSimpleMessage(e));
                }
            }
        });
        executableMapper.put(Integer.class, new UtilExecutable() {
            public Object execute(ResultSet resultSet, String columnName) {
                try {
                    return resultSet.getInt(columnName);
                } catch (SQLException e) {
                    throw new RuntimeException(ExceptionUtil.getSimpleMessage(e));
                }
            }
        });
    }

    public static <T> Field getPrimaryKeyField(Class<T> cls) {
        Field ret = null;
        for (Field field : cls.getDeclaredFields()) {
            if (field.getAnnotation(PrimaryKey.class) != null || field.getAnnotation(AutoIncrement.class) != null) {
                if (ret == null) {
                    ret = field;
                } else {
                    throw new RuntimeException("Duplicated primary key, eg. \"" + ret.getName() + "\" and \"" + field.getName() + "\"");
                }
            }
        }
        if (ret == null) {
            throw new RuntimeException("Cannot find any field which defined primary key");
        }
        return ret;
    }

    public static <T> String querySqlStatement(Class<T> cls) {
        String tableName = TableCreateHelper.getTableName(cls);
        return "SELECT * FROM " + tableName + ";";
    }

    public static <T> String querySqlStatement(Class<T> cls, Object id) {
        String tableName = TableCreateHelper.getTableName(cls);
        Field primaryKeyField = getPrimaryKeyField(cls);
        return "SELECT * FROM " + tableName + " WHERE " + primaryKeyField.getName() + " = " + TableInsertHelper.toSqlItemString(id) + ";";
    }

    public static <T> String querySqlStatement(Class<T> cls, List<Object> idSet) {
        Field primaryKeyField = getPrimaryKeyField(cls);
        String tableName = TableCreateHelper.getTableName(cls);
        StringBuffer buf = new StringBuffer("( ");
        boolean first = true;
        for (Object id : idSet) {
            if (!first) {
                buf.append(", ");
            }
            buf.append(TableInsertHelper.toSqlItemString(id));
            if (first) {
                first = false;
            }
        }
        buf.append(" )");
        return "SELECT * FROM " + tableName + " WHERE " + primaryKeyField.getName() + " in " + buf.toString() + ";";
    }

    public static <T> List<T> transformResultSet(Class<T> cls, ResultSet set) {
        List<T> retList = new ArrayList<T>();
        Map<String, Set<Method>> setMethods = new HashMap<>();
        for (Method method : cls.getDeclaredMethods()) {
            if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
                // register this set method to "setMethods"
                if (setMethods.containsKey(method.getName())) {
                    setMethods.get(method.getName()).add(method);
                } else {
                    Set<Method> s = new HashSet<>();
                    setMethods.put(method.getName(), s);
                }
            }
        }

        // traverse the result set
        boolean successNext = false;
        try {
            successNext = set.next();
        } catch (SQLException e) {
            throw new RuntimeException(ExceptionUtil.getSimpleMessage(e));
        }
        while (successNext) {
            T instance = ReflectionHelper.getInstance(cls);
            for (Field field : cls.getDeclaredFields()) {
                if (field.getAnnotation(Ignore.class) != null) {
                    continue;
                }
                String fieldName = field.getName();
                Class<?> fieldType = field.getType();
                if (!executableMapper.containsKey(fieldType)) {
                    throw new RuntimeException("Type of \"" + fieldType.getSimpleName() + "\" is not defined in executableMapper");
                }
                Object valueFromDatabase = executableMapper.get(fieldType).execute(set, fieldName);
                ReflectionHelper.setField(field, cls, instance, valueFromDatabase, setMethods);
            }
            retList.add(instance);
            try {
                successNext = set.next();
            } catch (SQLException e) {
                throw new RuntimeException(ExceptionUtil.getSimpleMessage(e));
            }
        }
        return retList;
    }

    public static <T> List<T> query(Class<T> cls) {
        String statement = querySqlStatement(cls);
        Logger.log(new Throwable(), Logger.INFO, statement, true);
        ResultSet rs = DatabaseUtil.executeQuery(statement);
        return transformResultSet(cls, rs);
    }

    public static <T> List<T> query(Class<T> cls, Object id) {
        String statement = querySqlStatement(cls, id);
        Logger.log(new Throwable(), Logger.INFO, statement, true);
        ResultSet rs = DatabaseUtil.executeQuery(statement);
        return transformResultSet(cls, rs);
    }

    public static <T> List<T> query(Class<T> cls, List<Object> idSet) {
        String statement = querySqlStatement(cls, idSet);
        Logger.log(new Throwable(), Logger.INFO, statement, true);
        ResultSet rs = DatabaseUtil.executeQuery(statement);
        return transformResultSet(cls, rs);
    }
}

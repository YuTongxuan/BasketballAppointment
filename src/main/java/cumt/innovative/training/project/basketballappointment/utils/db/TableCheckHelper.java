package cumt.innovative.training.project.basketballappointment.utils.db;

import cumt.innovative.training.project.basketballappointment.utils.CheckResult;
import cumt.innovative.training.project.basketballappointment.utils.DatabaseUtil;
import cumt.innovative.training.project.basketballappointment.utils.ExceptionUtil;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class TableCheckHelper {
    public static Map<String, String> tables;

    static {
        tables = new HashMap<>();
        ResultSet querySet = DatabaseUtil.executeQuery("select * from sqlite_master;");
        try {
            while (querySet.next()) {
                tables.put(querySet.getString("name"), querySet.getString("sql"));
            }
        }catch (Exception e) {
            throw new RuntimeException(ExceptionUtil.getSimpleMessage(e));
        }
    }

    public static CheckResult checkTable(Class<?> cls) {
        String createStatement = TableCreateHelper.getCreateStatement(cls);
        createStatement = createStatement.trim();
        String tableName = TableCreateHelper.getTableName(cls);
        if(!tables.containsKey(tableName)) {
            return CheckResult.TableNotExist;
        }
        String sqlCreateStatement = tables.get(tableName).trim();
        if(createStatement.endsWith(";")) {
            createStatement = createStatement.substring(0, createStatement.length() - 1);
            createStatement = createStatement.trim();
        }
        if(!createStatement.equals(sqlCreateStatement)) {
            return CheckResult.TableExistButTypeMismatch;
        }
        return CheckResult.CheckPass;
    }

}

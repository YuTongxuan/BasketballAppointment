package cumt.innovative.training.project.basketballappointment.utils.interfaces;

import java.sql.ResultSet;

public interface UtilExecutable {
    public Object execute(ResultSet resultSet, String columnName);
}
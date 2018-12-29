package cumt.innovative.training.project.basketballappointment.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cumt.innovative.training.project.basketballappointment.utils.ExceptionUtil;

public class ConfigurationModel {
    public static ConfigurationModel configuration = null;
    public final static String CONFIGURATION_PATH = "configuration.conf";

    public ConfigurationModel() {
    }

    public static ConfigurationModel getInstance() {
        if (configuration == null) {
            synchronized (ConfigurationModel.class) {
                if (configuration == null) {
                    configuration = new ConfigurationModel();
                }
            }
        }
        return configuration;
    }

    public String logFile = "basketball-appointment.log";
    public String databaseName = "basketball-data.db";
    public boolean logEnabled = true;
    public boolean consoleLogEnabled = true;
    public int logLevel = 0;
    public boolean databaseLogEnabled = true;
    public boolean injectQueryData = true;

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(ExceptionUtil.getSimpleMessage(e));
        }
    }
}

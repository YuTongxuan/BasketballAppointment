package cumt.innovative.training.project.basketballappointment.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import cumt.innovative.training.project.basketballappointment.config.ConfigurationModel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

public class ConfigurationUtil {
    public static void generateCommonConfigurationFile() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(ConfigurationModel.CONFIGURATION_PATH);
            mapper.writeValue(fileOutputStream, ConfigurationModel.getInstance());
        } catch (Exception ex) {
            throw new RuntimeException(ExceptionUtil.getSimpleMessage(ex));
        } finally {
            if(fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(ExceptionUtil.getSimpleMessage(e));
                }
            }
        }
    }

    public static void loadConfigurationFromFile() {
        ObjectMapper mapper = new ObjectMapper();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(ConfigurationModel.CONFIGURATION_PATH);
            ConfigurationModel configurationModel = mapper.readValue(fileInputStream, ConfigurationModel.class);
            // inject into ConfigurationModel singleton object by reflection
            Field field = ConfigurationModel.class.getDeclaredField("configuration");
            field.setAccessible(true);
            field.set(null, configurationModel);
        } catch (Exception ex) {
            throw new RuntimeException(ExceptionUtil.getSimpleMessage(ex));
        } finally {
            if(fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(ExceptionUtil.getSimpleMessage(e));
                }
            }
        }
    }

    public static void main(String[] args) {
        generateCommonConfigurationFile();
    }
}

package cumt.innovative.training.project.basketballappointment.utils;

import cumt.innovative.training.project.basketballappointment.logger.Logger;
import cumt.innovative.training.project.basketballappointment.model.Room;
import cumt.innovative.training.project.basketballappointment.model.User;
import cumt.innovative.training.project.basketballappointment.utils.db.TableCheckHelper;
import cumt.innovative.training.project.basketballappointment.utils.db.TableCreateHelper;
import cumt.innovative.training.project.basketballappointment.utils.db.TableInsertHelper;
import cumt.innovative.training.project.basketballappointment.utils.db.TableQueryHelper;

import java.util.List;

public class MainUtil {
    public static void preHandleForServerRun() {
        ConfigurationUtil.loadConfigurationFromFile();
        checkTables(User.class, Room.class);
        insertTestDataForUser();
//      insertTestDataForRoom();
    }

    private static void insertTestDataForUser() {
        User testUser1 = new User();
        testUser1.setAbility("1|3|4|2|6");
        testUser1.setAge(26);
        testUser1.setUserName("neko");
        testUser1.setPassword("1234567");
        testUser1.setGender("Male");
        testUser1.setImage("profile-img-1.png");
        boolean nekoExist = exists(User.class, new FilterByObject() {
            public boolean existCondition(Object arg) {
                return ((User) arg).getUserName().equals("neko");
            }
        });
        if (!nekoExist) {
            insertTestData(testUser1);
        }
    }

    private static void insertTestDataForRoom() {
        Room testRoom1 = new Room();
        testRoom1.setType(0);
        testRoom1.setCreator(1);
        testRoom1.setAppointmentTime("{from:{hour:9,minute:0},to:{hour:18,minute:0}}");
        testRoom1.setName("Nekos Room");
        testRoom1.setMembers("1|2|3");
        boolean nekoSRoomExists = exists(Room.class, new FilterByObject() {
            public boolean existCondition(Object arg) {
                return ((Room)arg).getName().equals("Nekos Room");
            }
        });
        if(!nekoSRoomExists) {
            insertTestData(testRoom1);
        }
    }

    public static void checkTables(Class<?>... classes) {
        boolean checkAllPass = true;
        for (Class<?> cls : classes) {
            CheckResult result = TableCheckHelper.checkTable(cls);
            int logLevel = result == CheckResult.CheckPass ? Logger.INFO : (result == CheckResult.TableNotExist ? Logger.WARNING : Logger.DANGER);
            Logger.log(new Throwable(), logLevel, cls.getSimpleName() + ", " + result);
            if (result == CheckResult.TableNotExist) {
                TableCreateHelper.createTable(cls);
            } else if (result == CheckResult.TableExistButTypeMismatch){
                checkAllPass = false;
            }
        }
        if(!checkAllPass) {
            throw new RuntimeException("Not all tables checked pass, exit...");
        }
    }

    public static void insertTestData(Object... objects) {
        for (Object object : objects) {
            try {
                TableInsertHelper.insert(object);
            } catch (RuntimeException ex) {
                Logger.log(new Throwable(), Logger.DANGER, ex.getMessage());
            }
        }
    }

    public static <T> boolean exists(Class<T> cls, FilterByObject filter) {
        List<T> data = null;
        try {
            data = TableQueryHelper.query(cls);
        } catch (RuntimeException ex) {
            Logger.log(new Throwable(), Logger.DANGER, ex.getMessage());
        }
        if(data == null) {
            return false;
        }
        for (Object object : data) {
            if (filter.existCondition(object)) {
                return true;
            }
        }
        return false;
    }
}

interface FilterByObject {
    public boolean existCondition(Object arg);
}
package cumt.innovative.training.project.basketballappointment.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import cumt.innovative.training.project.basketballappointment.model.User;
import cumt.innovative.training.project.basketballappointment.utils.annotation.*;
import cumt.innovative.training.project.basketballappointment.utils.db.ReflectionHelper;
import cumt.innovative.training.project.basketballappointment.utils.db.TableQueryHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class ControllerHelper {
    public static <T> T generateObjectByJsonString(String jsonString, Class<T> cls) {
        ObjectMapper mapper = new ObjectMapper();
        Map jsonObject = null;
        try {
            jsonObject = mapper.readValue(jsonString, Map.class);
        } catch (Exception e) {
            throw new RuntimeException(ExceptionUtil.getSimpleMessage(e));
        }
        Map<String, Set<Method>> setMethods = new HashMap<String, Set<Method>>();
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

        T instance = ReflectionHelper.getInstance(cls);
        for (Field field : cls.getDeclaredFields()) {
            if (field.getAnnotation(Ignore.class) != null) {
                continue;
            }
            if (field.getAnnotation(IgnoreWhenCreate.class) != null) {
                continue;
            }
            if (!jsonObject.keySet().contains(field.getName())) {
                continue;
            }
            String fieldName = field.getName();
            Object value = jsonObject.get(field.getName());
            ReflectionHelper.setField(field, cls, instance, value, setMethods);
        }
        return instance;
    }

    public static void injectForeignObjects(Object obj, Field field, Field target) {
        Map<String, Object> result = (Map<String, Object>) ReflectionHelper.getFieldValue(target, obj.getClass(), obj, true);
        boolean isNull = false;
        if(result == null) {
            result = new HashMap<>();
            isNull = true;
        }
        ForeignKey foreignKeyInfo = field.getAnnotation(ForeignKey.class);
        Class<?> cls = foreignKeyInfo.value();
        Field targetId = TableQueryHelper.getPrimaryKeyField(cls);
        Class<?> idType = targetId.getType();
        if (foreignKeyInfo.splitString().equals("null")) {
            // directly
            Object fieldValue = ReflectionHelper.getFieldValue(field, obj.getClass(), obj);
            List<?> searchedObject = TableQueryHelper.query(cls, fieldValue);
            if (searchedObject.size() == 0) {
                throw new RuntimeException("No result found of primary key: \"" + fieldValue + "\" in table \"" + cls.getSimpleName() + "\"");
            }
            result.put(field.getName(), searchedObject.get(0));
        } else {
            // split
            if (!field.getType().equals(String.class)) {
                throw new RuntimeException("Field \"" + field.getName() + "\" should be declared as String for it has splitString value");
            }
            String fieldValue = (String) (ReflectionHelper.getFieldValue(field, obj.getClass(), obj));
            String splitChar = foreignKeyInfo.splitString();
            if(splitChar.equals("|")) {
                splitChar = "\\|";
            }
            String[] parts = fieldValue.split(splitChar);
            List<Object> idSets = new ArrayList<>();
            for (String part : parts) {
                Object id = (idType.equals(int.class) || idType.equals(Integer.class)) ? Integer.parseInt(part) : part;
                idSets.add(id);
            }
            result.put(field.getName(), TableQueryHelper.query(cls, idSets));
        }
        if(isNull) {
            ReflectionHelper.setField(target, obj.getClass(), obj, result);
        }
    }

    public static void injectForeignObjects(Object obj) {
        Field targetField = null;
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.getAnnotation(ForeignKeyTarget.class) != null) {
                if (targetField != null) {
                    throw new RuntimeException("Find duplicated @ForeignKeyTarget in field \"" + targetField.getName() + "\" and \"" + field.getName() + "\"");
                }
                targetField = field;
            }
        }
        if (targetField == null) {
            throw new RuntimeException("Cannot find any field marked with @ForeignKeyTarget");
        }
        if (!targetField.getType().equals(Map.class)) {
            throw new RuntimeException("Field marked with @ForeignKeyTarget should declared with type Map<String, Object>");
        }
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.getAnnotation(ForeignKey.class) != null) {
                System.out.println(field.getName());
                injectForeignObjects(obj, field, targetField);
            }
        }
    }

    public static <T> void injectForeignObjectList(List<T> objects) {
        for (T object : objects) {
            injectForeignObjects(object);
        }
    }
}

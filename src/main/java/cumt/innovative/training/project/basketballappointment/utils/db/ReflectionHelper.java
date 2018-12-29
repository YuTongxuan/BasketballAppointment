package cumt.innovative.training.project.basketballappointment.utils.db;

import cumt.innovative.training.project.basketballappointment.utils.ExceptionUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ReflectionHelper {
    public static <T> T getInstance(Class<T> cls) {
        // try to new a cls instance
        T instance = null;
        // 1. get default constructor, eg, public User();
        try {
            Constructor<T> constructor = cls.getDeclaredConstructor();
            instance = constructor.newInstance();
        } catch (Exception e) {
            System.out.println(ExceptionUtil.getSimpleMessage(e));
        }
        // 2. user method "newInstance"
        if (instance == null) {
            try {
                instance = cls.newInstance();
            } catch (Exception e) {
                System.out.println(ExceptionUtil.getSimpleMessage(e));
            }
        }
        if (instance == null) {
            throw new RuntimeException("Cannot call default constructor of class \"" + cls.getSimpleName() + "\"");
        }
        return instance;
    }

    public static <T> void setField(Field field, Class<T> parentCls, Object instance, Object value) {
        setField(field, parentCls, instance, value, null);
    }

    public static <T> void setField(Field field, Class<T> parentCls, Object instance, Object value, Map<String, Set<Method>> setMethods) {
        String fieldName = field.getName();
        if(setMethods == null) {
            setMethods = new HashMap<String, Set<Method>>();
            for (Method method : parentCls.getDeclaredMethods()) {
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
        }
        // 1. try to set filed by set method, eg. void setName(String name)
        boolean successInFirstStep = false;
        String setMethodName = "set" + ("" + fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
        if (setMethods.containsKey(setMethodName)) {
            Set<Method> candidates = setMethods.get(setMethodName);
            if (candidates.size() == 1) {
                Method m = candidates.iterator().next();
                if (Modifier.isPrivate(m.getModifiers())) {
                    m.setAccessible(true);
                }
                try {
                    m.invoke(instance, value);
                    successInFirstStep = true;
                } catch (Exception e) {
                    System.out.println(ExceptionUtil.getSimpleMessage(e));
                }
            }
        }
        // 2. set the value to field directly
        boolean successInSecondStep = false;
        if (!successInFirstStep) {
            if (Modifier.isPrivate(field.getModifiers())) {
                field.setAccessible(true);
            }
            try {
                field.set(instance, value);
                successInFirstStep = true;
            } catch (IllegalAccessException e) {
                System.out.println(ExceptionUtil.getSimpleMessage(e));
            }
        }
        if (!successInFirstStep && !successInSecondStep) {
            throw new RuntimeException("Cannot set the value of field \"" + fieldName + "\"");
        }
    }

    public static <T> Object getFieldValue(Field field, Class<T> parentCls, Object instance) {
        return getFieldValue(field, parentCls, instance, false);
    }

    public static <T> Object getFieldValue(Field field, Class<T> parentCls, Object instance, boolean allowNull) {
        String fieldName = field.getName();
        Object value = null;
        // get field value
        // 1. search for get method, for example, int getId() {}
        String getMethodName = "get" + ("" + fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
        Method getMethod = null;
        try {
            getMethod = parentCls.getDeclaredMethod(getMethodName);
            if (Modifier.isPrivate(getMethod.getModifiers())) {
                getMethod.setAccessible(true);
            }
            value = getMethod.invoke(instance);
        } catch (Exception e) {
            System.out.println(ExceptionUtil.getSimpleMessage(e));
        }
        // 2. read the field directly
        if (getMethod == null) {
            if (Modifier.isPrivate(field.getModifiers())) {
                field.setAccessible(true);
            }
            try {
                value = field.get(instance);
            } catch (Exception e) {
                System.out.println(ExceptionUtil.getSimpleMessage(e));
            }
        }
        if (value == null && !allowNull) {
            throw new RuntimeException("Cannot get the value of field \"" + fieldName + "\"");
        }
        return value;
    }
}

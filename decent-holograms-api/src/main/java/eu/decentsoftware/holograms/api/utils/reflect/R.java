package eu.decentsoftware.holograms.api.utils.reflect;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class R {

    private static String version;
    private final Object object;

    public R(Object object) {
        this.object = object;
    }

    public void invoke(String methodName, Object... args) {
        try {
            Method method = object.getClass().getMethod(methodName, Arrays.stream(args).map(Object::getClass).toArray(Class[]::new));
            method.setAccessible(true);
            method.invoke(object, args);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Object get(String fieldName) {
        try {
            Field field = object.getClass().getField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void set(String fieldName, Object value) {
        try {
            Field field = object.getClass().getField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /*
     *  ============== Static methods ==============
     */

    public static String getVersion() {
        if (version == null) {
            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];;
        }
        return version;
    }

    public static Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getNMClass(String name) {
        try {
            return Class.forName("net.minecraft." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + getVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getObcClass(String classname) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + classname);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> boolean setFieldValue(@NotNull Object object, String fieldName, T value) {
        Class<?> clazz = object instanceof Class<?> ? (Class<?>) object : object.getClass();
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
            return true;
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
            return false;
        }
    }

    @Nullable
    public static <T> T getFieldValue(@NotNull Object object, String fieldName) {
        Class<?> clazz = object instanceof Class<?> ? (Class<?>) object : object.getClass();
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
            return null;
        }
    }

}

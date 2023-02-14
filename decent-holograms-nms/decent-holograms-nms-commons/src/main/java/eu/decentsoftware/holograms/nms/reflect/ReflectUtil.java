/*
 * DecentHolograms
 * Copyright (C) DecentSoftware.eu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.decentsoftware.holograms.nms.reflect;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Utility class for working with reflection.
 *
 * @author d0by
 */
public class ReflectUtil {

    private static String version;
    private final @NotNull Object object;

    /**
     * Creates a new instance of {@link ReflectUtil} with the given object.
     *
     * @param object The object to work with.
     */
    public ReflectUtil(@NotNull Object object) {
        this.object = object;
    }

    /**
     * Invokes a method with the given name and arguments.
     *
     * @param methodName The name of the method to invoke.
     * @param args       The arguments to pass to the method.
     */
    public void invoke(String methodName, Object... args) {
        try {
            Method method = object.getClass().getMethod(methodName, Arrays.stream(args).map(Object::getClass).toArray(Class[]::new));
            method.setAccessible(true);
            method.invoke(object, args);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the value of the field with the given name.
     *
     * @param fieldName The name of the field to get the value of.
     * @return The value of the field.
     */
    public Object get(String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sets the value of the field with the given name.
     *
     * @param fieldName The name of the field to set the value of.
     * @param value     The value to set.
     */
    public void set(String fieldName, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /*
     *  ============== Static methods ==============
     */

    /**
     * Gets the version of the server.
     *
     * @return The version of the server.
     */
    public static String getVersion() {
        if (version == null) {
            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        }
        return version;
    }

    /**
     * Gets the class of the given name.
     *
     * @param name The name of the class.
     * @return The class of the given name.
     */
    @Nullable
    public static Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the class of the given name from the "net.minecraft" package.
     *
     * @param name The name of the class.
     * @return The class of the given name.
     */
    @Nullable
    public static Class<?> getNMClass(String name) {
        try {
            return Class.forName("net.minecraft." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the class of the given name from the "net.minecraft.server.VERSION" package.
     *
     * @param name The name of the class.
     * @return The class of the given name.
     */
    @Nullable
    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + getVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the class of the given name from the "org.bukkit.craftbukkit.VERSION" package.
     *
     * @param name The name of the class.
     * @return The class of the given name.
     */
    @Nullable
    public static Class<?> getObcClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sets the value of the field with the given name in the given object.
     *
     * @param object    The object to set the value of the field in.
     * @param fieldName The name of the field to set the value of.
     * @param value     The value to set.
     * @param <T>       The type of the value.
     * @return Whether the field was successfully set.
     */
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

    /**
     * Gets the value of the field with the given name in the given object.
     *
     * @param object    The object to get the value of the field from.
     * @param fieldName The name of the field to get the value of.
     * @param <T>       The type of the value.
     * @return The value of the field.
     */
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

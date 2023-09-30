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

package eu.decentsoftware.holograms.nms.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@UtilityClass
public class ReflectUtil {

    private static String version;

    /**
     * Set the value of a field with the given name on the given object.
     * <p>
     * If the field is not found, or is not accessible, this method will return false.
     *
     * @param object    The object to set the field on.
     * @param fieldName The name of the field to set.
     * @param value     The value to set the field to.
     * @param <T>       The type of the field and value.
     * @return True if the field was found and set, false otherwise.
     */
    public static <T> boolean setFieldValue(final @NotNull Object object, final @NotNull String fieldName, final @Nullable T value) {
        Class<?> clazz = object.getClass();
        Field field = getField(clazz, fieldName);
        if (field != null) {
            try {
                field.set(object, value);
                return true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Get the value of a field with the given name from the given object.
     * <p>
     * If the field is not found, or is not accessible, this method will return null.
     *
     * @param object    The object to get the field from.
     * @param fieldName The name of the field to get.
     * @param <T>       The type of the field.
     * @return The value of the field, or null if the field was not found.
     */
    @Nullable
    public static <T> T getFieldValue(final @NotNull Object object, final @NotNull String fieldName) {
        Class<?> clazz = object instanceof Class<?> ? (Class<?>) object : object.getClass();
        Field field = getField(clazz, fieldName);
        if (field != null) {
            try {
                //noinspection unchecked
                return (T) field.get(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Get a field with the given name from the given class.
     * <p>
     * If the field is not found, this method will return null.
     *
     * @param clazz     The class to get the field from.
     * @param fieldName The name of the field to get.
     * @return The field, or null if the field was not found.
     */
    @Nullable
    public static Field getField(final @NotNull Class<?> clazz, final @NotNull String fieldName) {
        Field field;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            try {
                // Try superclass
                field = clazz.getField(fieldName);
            } catch (NoSuchFieldException ex) {
                // Field does not exist
                ex.printStackTrace();
                return null;
            }
        }

        field.setAccessible(true);
        return field;
    }

    public static Method getMethod(final @NotNull Class<?> clazz, final @NotNull String methodName, final Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }

    /**
     * Get the NMS version of the server.
     * <p>
     * This is the version of the server, such as v1_8_R3. This is used for reflection.
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
     * Get a class by class path. The classPath should be the full path, including the package.
     *
     * @param classPath The path of the class to get.
     * @return The class, or null if the class was not found.
     */
    @Nullable
    public static Class<?> getClass(final @NotNull String classPath) {
        try {
            return Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a class from the {@code net.minecraft} package. The classPath should be the full path to the class,
     * including the package without the {@code net.minecraft} prefix.
     * <p>
     * This is a shortcut for {@code getClass("net.minecraft." + classPath)}.
     * <p>
     * If you are looking for the old {@code net.minecraft.server.<VERSION>} package,
     * use {@link #getNMSClass(String)}.
     *
     * @param classPath The path of the class to get.
     * @return The class, or null if the class was not found.
     * @see #getNMSClass(String)
     */
    @Nullable
    public static Class<?> getNMClass(final @NotNull String classPath) {
        try {
            return Class.forName("net.minecraft." + classPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a class from the {@code net.minecraft.server.<VERSION>} package. The classPath should be the full path to the class,
     * including the package without the {@code net.minecraft.server.<VERSION>} prefix.
     * <p>
     * This is a shortcut for {@code getClass("net.minecraft.server." + getVersion() + "." + classPath)}.
     * <p>
     * If you are looking for the new {@code net.minecraft} package, use {@link #getNMClass(String)}.
     *
     * @param classPath The path of the class to get.
     * @return The class, or null if the class was not found.
     */
    @Nullable
    public static Class<?> getNMSClass(final @NotNull String classPath) {
        try {
            return Class.forName("net.minecraft.server." + getVersion() + "." + classPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a class from the {@code org.bukkit.craftbukkit.<VERSION>} package. The classPath should be the full path to the class,
     * including the package without the {@code org.bukkit.craftbukkit.<VERSION>} prefix.
     * <p>
     * This is a shortcut for {@code getClass("org.bukkit.craftbukkit." + getVersion() + "." + name)}.
     *
     * @param classPath The path of the class to get.
     * @return The class, or null if the class was not found.
     */
    @Nullable
    public static Class<?> getObcClass(final @NotNull String classPath) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + classPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}

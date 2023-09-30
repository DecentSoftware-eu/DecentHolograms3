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

import lombok.NonNull;

import java.lang.reflect.Field;

public class ReflectField {

    private final Field field;

    public ReflectField(final @NonNull Class<?> clazz, final @NonNull String fieldName) {
        try {
            Field field;
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (final NoSuchFieldException e) {
                field = clazz.getField(fieldName);
            }
            this.field = field;
            this.field.setAccessible(true);
        } catch (final NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public Object get(final Object instance) {
        try {
            return this.field.get(instance);
        } catch (final IllegalAccessException e) {
            return null;
        }
    }

    public void set(final Object instance, final Object value) {
        try {
            this.field.set(instance, value);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}

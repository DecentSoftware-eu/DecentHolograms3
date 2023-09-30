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

package eu.decentsoftware.holograms.commands.framework.arguments;

import eu.decentsoftware.holograms.commands.framework.DecentCommand;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is used for parsing command arguments from a list of raw strings. It
 * is used by {@link DecentCommand} to parse arguments.
 * <p>
 * When parsing arguments, a pointer is kept to the current index of the argument
 * being parsed. This pointer is incremented as arguments are parsed. This means
 * that arguments must be parsed in the order they are expected to be in. For
 * example, if you expect the first argument to be a string and the second to be
 * an integer, you must parse the string first and then the integer. Also, if you
 * parse an argument, you will not be able to parse any arguments before it or the
 * same argument again.
 *
 * @author d0by
 * @see DecentCommand
 * @since 3.0.0
 */
@SuppressWarnings("unused")
public class Arguments {

    /**
     * The index of the next argument to parse.
     */
    private final @NonNull AtomicInteger index = new AtomicInteger(0);
    private final @NonNull List<String> rawArguments;

    /**
     * Create a new instance of {@link Arguments} with the given raw arguments.
     *
     * @param rawArguments The raw arguments.
     */
    public Arguments(final @NonNull List<String> rawArguments) {
        this.rawArguments = rawArguments;
    }

    /**
     * Get the total number of raw arguments.
     *
     * @return The total number of raw arguments.
     */
    public int size() {
        return rawArguments.size();
    }

    /**
     * Get the number of raw arguments that are left to be parsed.
     *
     * @return The number of raw arguments that are left to be parsed.
     */
    public int remaining() {
        return rawArguments.size() - index.get();
    }

    /**
     * Move the index by the given offset. If the index is moved to a negative
     * value, it will be set to 0.
     *
     * @param offset The offset to move the index by.
     */
    public void moveIndex(final int offset) {
        if (index.addAndGet(offset) < 0) {
            index.set(0);
        }
    }

    /**
     * Get the next raw argument. If there are no more arguments, an empty optional
     * is returned.
     *
     * @return The next raw argument, or an empty optional if there are no more
     * arguments.
     */
    public Optional<String> peek() {
        return peek(0);
    }

    /**
     * Get the next raw argument. If there are no more arguments, an empty optional
     * is returned.
     *
     * @return The next raw argument, or an empty optional if there are no more
     * arguments.
     */
    public Optional<String> peek(final int offset) {
        if (index.get() + offset >= rawArguments.size()) {
            return Optional.empty();
        }
        return Optional.of(rawArguments.get(index.get() + offset));
    }

    /**
     * Get the next argument as an instance of the given class. If there are no
     * more arguments or there is no parser for the given class, an empty
     * optional is returned.
     *
     * @param clazz The class to parse the argument as.
     * @param <T>   The type of the class.
     * @return The next argument as an instance of the given class, or an empty
     * optional if there are no more arguments or there is no parser for the given
     * class.
     */
    @SuppressWarnings("unchecked")
    @NonNull
    public <T> Optional<T> next(final @NonNull Class<T> clazz) {
        ArgumentParser<T> parser = (ArgumentParser<T>) ArgumentParser.PARSERS.get(clazz);
        if (parser == null) {
            return Optional.empty();
        }
        return parser.parse(this);
    }

    /**
     * Get the next argument as a string. If there are no more arguments, an empty
     * optional is returned.
     *
     * @return The next argument as a string, or an empty optional if there are no
     * more arguments.
     * @see #nextString(boolean)
     */
    @NonNull
    public Optional<String> nextString() {
        return nextString(false);
    }

    /**
     * Get the next argument as a string. If there are no more arguments, an empty
     * optional is returned.
     *
     * @param greedy Whether to consume all remaining arguments. This will return
     *               all remaining arguments as a single string, separated by
     *               spaces.
     * @return The next argument as a string, or an empty optional if there are no
     * more arguments.
     * @see #nextString()
     */
    @NonNull
    public Optional<String> nextString(final boolean greedy) {
        if (index.get() >= rawArguments.size()) {
            return Optional.empty();
        }
        if (greedy) {
            final StringBuilder builder = new StringBuilder();
            for (int i = index.get(); i < rawArguments.size(); i++) {
                builder.append(rawArguments.get(i)).append(" ");
            }
            index.set(rawArguments.size());
            return Optional.of(builder.toString().trim());
        }
        final String next = rawArguments.get(index.getAndIncrement());
        return Optional.of(next);
    }

    /**
     * Get the next argument as an integer. If there are no more arguments, or the
     * next argument is not an integer, an empty optional is returned.
     *
     * @return The next argument as an integer, or an empty optional if there are no
     * more arguments or the next argument is not an integer.
     * @see #nextInteger(int, int)
     */
    @NonNull
    public Optional<Integer> nextInteger() {
        try {
            return nextString().map(Integer::parseInt);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Get the next argument as an integer. If there are no more arguments, or the
     * next argument is not an integer, or the next argument is not between the
     * given min and max values, an empty optional is returned.
     *
     * @param min The minimum value the integer can be. (inclusive)
     * @param max The maximum value the integer can be. (inclusive)
     * @return The next argument as an integer, or an empty optional if there are no
     * more arguments or the next argument is not an integer or the next argument
     * is not between the given min and max values.
     * @see #nextInteger()
     */
    @NonNull
    public Optional<Integer> nextInteger(final int min, final int max) {
        return nextInteger().filter(i -> i >= min && i <= max);
    }

    /**
     * Get the next argument as a double. If there are no more arguments, or the
     * next argument is not a double, an empty optional is returned.
     *
     * @return The next argument as a double, or an empty optional if there are no
     * more arguments or the next argument is not a double.
     * @see #nextDouble(double, double)
     */
    @NonNull
    public Optional<Double> nextDouble() {
        try {
            return nextString().map(Double::parseDouble);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Get the next argument as a double. If there are no more arguments, or the
     * next argument is not a double, or the next argument is not between the given
     * min and max values, an empty optional is returned.
     *
     * @param min The minimum value the double can be. (inclusive)
     * @param max The maximum value the double can be. (inclusive)
     * @return The next argument as a double, or an empty optional if there are no
     * more arguments or the next argument is not a double or the next argument is
     * not between the given min and max values.
     * @see #nextDouble()
     */
    @NonNull
    public Optional<Double> nextDouble(final double min, final double max) {
        return nextDouble().filter(d -> d >= min && d <= max);
    }

    /**
     * Get the next argument as a float. If there are no more arguments, or the
     * next argument is not a float, an empty optional is returned.
     *
     * @return The next argument as a float, or an empty optional if there are no
     * more arguments or the next argument is not a float.
     * @see #nextFloat(float, float)
     */
    @NonNull
    public Optional<Float> nextFloat() {
        try {
            return nextString().map(Float::parseFloat);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Get the next argument as a float. If there are no more arguments, or the
     * next argument is not a float, or the next argument is not between the given
     * min and max values, an empty optional is returned.
     *
     * @param min The minimum value the float can be. (inclusive)
     * @param max The maximum value the float can be. (inclusive)
     * @return The next argument as a float, or an empty optional if there are no
     * more arguments or the next argument is not a float or the next argument is
     * not between the given min and max values.
     * @see #nextFloat()
     */
    @NonNull
    public Optional<Float> nextFloat(final float min, final float max) {
        return nextFloat().filter(f -> f >= min && f <= max);
    }

    /**
     * Get the next argument as a long. If there are no more arguments, or the next
     * argument is not a long, an empty optional is returned.
     *
     * @return The next argument as a long, or an empty optional if there are no
     * more arguments or the next argument is not a long.
     * @see #nextLong(long, long)
     */
    @NonNull
    public Optional<Long> nextLong() {
        try {
            return nextString().map(Long::parseLong);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Get the next argument as a long. If there are no more arguments, or the next
     * argument is not a long, or the next argument is not between the given min
     * and max values, an empty optional is returned.
     *
     * @param min The minimum value the long can be. (inclusive)
     * @param max The maximum value the long can be. (inclusive)
     * @return The next argument as a long, or an empty optional if there are no
     * more arguments or the next argument is not a long or the next argument is
     * not between the given min and max values.
     * @see #nextLong()
     */
    @NonNull
    public Optional<Long> nextLong(final long min, final long max) {
        return nextLong().filter(l -> l >= min && l <= max);
    }

    /**
     * Get the next argument as a short. If there are no more arguments, or the
     * next argument is not a short, an empty optional is returned.
     *
     * @return The next argument as a short, or an empty optional if there are no
     * more arguments or the next argument is not a short.
     * @see #nextShort(short, short)
     */
    @NonNull
    public Optional<Short> nextShort() {
        try {
            return nextString().map(Short::parseShort);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Get the next argument as a short. If there are no more arguments, or the
     * next argument is not a short, or the next argument is not between the given
     * min and max values, an empty optional is returned.
     *
     * @param min The minimum value the short can be. (inclusive)
     * @param max The maximum value the short can be. (inclusive)
     * @return The next argument as a short, or an empty optional if there are no
     * more arguments or the next argument is not a short or the next argument is
     * not between the given min and max values.
     * @see #nextShort()
     */
    @NonNull
    public Optional<Short> nextShort(final short min, final short max) {
        return nextShort().filter(s -> s >= min && s <= max);
    }

    /**
     * Get the next argument as a byte. If there are no more arguments, or the next
     * argument is not a byte, an empty optional is returned.
     *
     * @return The next argument as a byte, or an empty optional if there are no
     * more arguments or the next argument is not a byte.
     * @see #nextByte(byte, byte)
     */
    @NonNull
    public Optional<Byte> nextByte() {
        try {
            return nextString().map(Byte::parseByte);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Get the next argument as a byte. If there are no more arguments, or the next
     * argument is not a byte, or the next argument is not between the given min
     * and max values, an empty optional is returned.
     *
     * @param min The minimum value the byte can be. (inclusive)
     * @param max The maximum value the byte can be. (inclusive)
     * @return The next argument as a byte, or an empty optional if there are no
     * more arguments or the next argument is not a byte or the next argument is
     * not between the given min and max values.
     * @see #nextByte()
     */
    @NonNull
    public Optional<Byte> nextByte(final byte min, final byte max) {
        return nextByte().filter(b -> b >= min && b <= max);
    }

    /**
     * Get the next argument as a boolean. If there are no more arguments, or the
     * next argument is not a boolean, an empty optional is returned.
     *
     * @return The next argument as a boolean, or an empty optional if there are no
     * more arguments or the next argument is not a boolean.
     */
    @NonNull
    public Optional<Boolean> nextBoolean() {
        return nextString(false).map(Boolean::parseBoolean);
    }

    /**
     * Get the next argument as a character. If there are no more arguments, or the
     * next argument is not a character, an empty optional is returned.
     *
     * @return The next argument as a character, or an empty optional if there are
     * no more arguments or the next argument is not a character.
     */
    @NonNull
    public Optional<Character> nextCharacter() {
        return nextString(false)
                .filter(s -> s.length() == 1)
                .map(s -> s.charAt(0));
    }

    public boolean extractFlag(String... aliases) {
        return extractAnyFlag("--", aliases);
    }

    public boolean extractShortFlag(String... aliases) {
        return extractAnyFlag("-", aliases);
    }

    private boolean extractAnyFlag(String prefix, String... aliases) {
        final int index = this.index.get();
        for (int i = 0; i < rawArguments.size(); i++) {
            String arg = rawArguments.get(i);
            if (!arg.startsWith(prefix)) {
                continue;
            }

            String flag = arg.substring(prefix.length());
            for (String alias : aliases) {
                if (!flag.equalsIgnoreCase(alias)) {
                    break;
                }

                rawArguments.remove(i);
                if (i < index) {
                    this.index.decrementAndGet();
                }
                return true;
            }
        }
        return false;
    }

}

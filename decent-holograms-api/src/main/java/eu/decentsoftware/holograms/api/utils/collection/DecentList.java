package eu.decentsoftware.holograms.api.utils.collection;

import org.jetbrains.annotations.Contract;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class represents a list with some useful methods.
 *
 * @param <T> Type of the items.
 * @author d0by
 */
@SuppressWarnings("unused")
public class DecentList<T> extends ArrayList<T> {

    /**
     * Create a new {@link DecentList}.
     *
     * @param initialCapacity Initial capacity of this list.
     * @see ArrayList#ArrayList(int)
     */
    public DecentList(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Create a new {@link DecentList}.
     *
     * @param collection Collection of items.
     */
    public DecentList(Collection<T> collection) {
        super();
        this.add(collection);
    }

    /**
     * Create a new {@link DecentList}.
     *
     * @param array Array of items.
     */
    @SafeVarargs
    public DecentList(T... array) {
        super();
        this.add(array);
    }

    /**
     * Add all items from the given array.
     *
     * @param ts The array.
     */
    @Contract(mutates = "this")
    @SafeVarargs
    public final void add(T... ts) {
        if (ts != null && ts.length > 0) {
            this.addAll(Arrays.asList(ts));
        }
    }

    /**
     * Add all items from the given collection.
     *
     * @param collection The collection.
     */
    public void add(Collection<T> collection) {
        if (collection != null && !collection.isEmpty()) {
            this.addAll(collection);
        }
    }

    /**
     * Add a new item at the first index of this list.
     *
     * @param t The item.
     */
    public void addFirst(T t) {
        add(0, t);
    }

    /**
     * Get the first item of this list.
     *
     * @return The item.
     */
    public T first() {
        if (size() < 1) {
            return null;
        }
        return get(0);
    }

    /**
     * Get a random item from this list.
     *
     * @return The item.
     */
    public T random() {
        return get(randomIndex());
    }

    /**
     * Get the last item of this list.
     *
     * @return The item.
     */
    public T last() {
        if (size() < 1) {
            return null;
        }
        return get(lastIndex());
    }

    /**
     * Pop the first item off this list and return it
     *
     * @return The item or null if the list is empty
     */
    public T pop() {
        if (isEmpty()) return null;
        return remove(0);
    }

    /**
     * Pop the last item off this list and return it
     *
     * @return The item or null if the list is empty
     */
    public T popLast() {
        if (isEmpty()) return null;
        return remove(lastIndex());
    }

    /**
     * Pop a random item from this list and return it.
     *
     * @return The item or null if the list is empty.
     */
    public T popRandom() {
        if (isEmpty()) return null;
        if (size() == 1) {
            return pop();
        }
        return remove(randomIndex());
    }

    /**
     * Check whether this list is NOT empty.
     *
     * @return The requested boolean.
     */
    public boolean isNotEmpty() {
        return !isEmpty();
    }

    /**
     * Check whether this list is NOT empty.
     *
     * @return The requested boolean.
     */
    public boolean hasElements() {
        return !isEmpty();
    }

    /**
     * Get the index of the last item.
     *
     * @return index of the last item.
     */
    public int lastIndex() {
        return size() - 1;
    }

    /**
     * Get a random index in bounds of this list.
     *
     * @return The index.
     */
    public int randomIndex() {
        return ThreadLocalRandom.current().nextInt(0, size());
    }

    /**
     * Check whether this list has any duplicate items.
     *
     * @return The requested boolean.
     */
    public boolean hasDuplicates() {
        return size() != new LinkedHashSet<>(this).size();
    }

    /**
     * Check whether the given index is in bounds of this list.
     *
     * @param index The index.
     * @return The requested boolean.
     */
    public boolean inBounds(int index) {
        return index >= 0 && index < size();
    }

    /**
     * Create a copy of this list and return it.
     *
     * @return The copy.
     */
    public DecentList<T> copy() {
        return new DecentList<>(this);
    }

    /**
     * Sort this list.
     *
     * @return Instance of this.
     */
    public DecentList<T> sort() {
        sort(null);
        return this;
    }

    /**
     * Create a copy of this list and sort it.
     *
     * @return The copy.
     */
    public DecentList<T> sortCopy() {
        DecentList<T> list = copy();
        list.sort(null);
        return list;
    }

    /**
     * Shuffle this list.
     *
     * @return Instance of this.
     */
    public DecentList<T> shuffle() {
        Collections.shuffle(this);
        return this;
    }

    /**
     * Create a copy of this list and shuffle it.
     *
     * @return The copy.
     */
    public DecentList<T> shuffleCopy() {
        DecentList<T> list = copy();
        Collections.shuffle(list);
        return list;
    }

    /**
     * Reverse this list.
     *
     * @return Instance of this.
     */
    public DecentList<T> reverse() {
        Collections.reverse(this);
        return this;
    }

    /**
     * Create a copy of this list and reverse it.
     *
     * @return The copy.
     */
    public DecentList<T> reverseCopy() {
        DecentList<T> list = copy();
        Collections.reverse(list);
        return list;
    }

    /**
     * Create a copy of this list and parse all of its items to Strings.
     *
     * @return The copy.
     */
    public DecentList<String> toStringList() {
        DecentList<String> list = new DecentList<>();
        for (T t : this) {
            list.add(t.toString());
        }
        return list;
    }

    /**
     * Get a sublist.
     *
     * @param start The start index. (Including)
     * @param end   The end index. (Excluding)
     * @return The sublist.
     */
    @Override
    public DecentList<T> subList(int start, int end) {
        DecentList<T> list = new DecentList<>();
        for (int i = start; i < Math.min(size(), end); i++) {
            list.add(get(i));
        }
        return list;
    }

}

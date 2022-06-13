package eu.decent.holograms.api.utils.collection;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;

public interface Registry<V> extends Iterable<V> {

    void clear();
    Collection<V> getValues();

    default void shutdown() {
        clear();
    }

    @NotNull
    @Override
    default Iterator<V> iterator() {
        return getValues().iterator();
    }

    @Override
    default Spliterator<V> spliterator() {
        return getValues().spliterator();
    }

}

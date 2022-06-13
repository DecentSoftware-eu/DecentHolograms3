package eu.decent.holograms.api.utils.collection;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CollectionRegistry<V> implements Registry<V> {

    private final List<V> rootList;

    public CollectionRegistry() {
        this(Collections.synchronizedList(Lists.newArrayList()));
    }

    public CollectionRegistry(final List<V> initial) {
        this.rootList = initial;
    }

    @Override
    public void clear() {
        rootList.clear();
    }

    @Override
    public Collection<V> getValues() {
        return rootList;
    }

}

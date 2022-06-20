package eu.decentsoftware.holograms.api.utils.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represents a registry that maps keys to values.
 *
 * @param <K> Class of the key.
 * @param <V> Class of the value.
 * @author d0by
 */
@SuppressWarnings("unused")
public abstract class Registry<K, V> {

    protected final Map<K, V> map;

    /**
     * Create a new instance of {@link Registry}.
     */
    public Registry() {
        this.map = new ConcurrentHashMap<>();
    }

    /**
     * Reload this registry.
     */
    public abstract void reload();

    /**
     * Shutdown this registry.
     */
    public void shutdown() {
        this.map.clear();
    }

    /**
     * Clear this registry.
     */
    public void clear() {
        map.clear();
    }

    /**
     * Register a new value into this registry.
     *
     * @param key The key.
     * @param value The value.
     */
    public void register(K key, V value) {
        map.put(key, value);
    }

    /**
     * Get a value by key.
     *
     * @param key The key.
     */
    public V get(K key) {
        return map.get(key);
    }

    /**
     * Remove a value by key.
     *
     * @param key The key.
     * @return The value.
     */
    public V remove(K key) {
        return map.remove(key);
    }

    /**
     * Check whether this registry contains the given key.
     *
     * @param key The key.
     * @return The requested boolean.
     */
    public boolean contains(K key) {
        return map.containsKey(key);
    }

    /**
     * Get the values in this registry.
     *
     * @return Collection of the values.
     */
    public Collection<V> getValues() {
        return map.values();
    }

    /**
     * Get the keys in this registry.
     *
     * @return Set of the keys.
     */
    public Set<K> getKeys() {
        return map.keySet();
    }

}

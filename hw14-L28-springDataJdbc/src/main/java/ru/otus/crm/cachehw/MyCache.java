package ru.otus.crm.cachehw;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.List;
import java.util.ArrayList;

public class MyCache<K, V> implements HwCache<K, V> {
    private Map<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();
    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notify(key, value, "put");
    }
    @Override
    public void remove(K key) {
        V value = cache.get(key);
        notify(key, value, "remove");
        cache.remove(key);
    }

    @Override
    public V get(K key){
        V value = cache.get(key);
        System.out.println("Size of cache: " + cache.size() + ", get key=" + key + " value=" + value);
        notify(key, value, "get");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }
    public void notify(K key, V value, String action) {
        for (HwListener<K, V> listener : listeners) {
            listener.notify(key, value, action);
        }
    }
}
package am.s_mukhamedzhanov.sd;

import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> {

    final int capacity;
    final Map<K, ValueWithRef<K, V>> entries;
    final LRUList<K> list;

    public LRUCache(int capacity) {
        assert capacity > 0;
        this.capacity = capacity;
        entries = new HashMap<>();
        list = new LRUList<>();
    }

    public void put(K key, V value) {
        assert key != null && value != null;
        if (size() == capacity) {
            K last = list.deleteLast();
            entries.remove(last);
        }
        assert size() < capacity;
        boolean isInCache = entries.containsKey(key);
        if (isInCache) {
            ValueWithRef<K, V> kvValueWithRef = entries.get(key);
            kvValueWithRef.value = value;
            list.propagateNode(kvValueWithRef.ref);
        } else {
            LRUList.Node<K> kNode = list.addFirst(key);
            entries.put(key, new ValueWithRef<>(value, kNode));
        }
    }

    public V get(K key) {
        assert key != null;
        if (entries.containsKey(key)) {
            ValueWithRef<K, V> kvValueWithRef = entries.get(key);
            list.propagateNode(kvValueWithRef.ref);
            assert size() <= capacity;
            return kvValueWithRef.value;
        }
        return null;
    }


    int size() {
        assert list.size == entries.size();
        return entries.size();
    }


    public static class ValueWithRef<K, V> {
        V value;
        LRUList.Node<K> ref;

        public ValueWithRef(V value, LRUList.Node<K> ref) {
            this.value = value;
            this.ref = ref;
        }
    }


}

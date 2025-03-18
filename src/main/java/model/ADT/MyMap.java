package model.ADT;

import exceptions.ADTException;
import exceptions.KeyNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyMap<K, V> implements MyIMap<K, V> {
    private Map<K, V> map;

    public MyMap() {
        map = new HashMap<>();
    }

    public MyMap(Map<K, V> map) {
        this.map = new HashMap<>(map);
    }

    @Override
    public K getKey(V value) throws ADTException {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        throw new KeyNotFoundException();
    }

    @Override
    public V getValue(K key) throws ADTException {
        if (!map.containsKey(key)) {
            throw new KeyNotFoundException();
        }
        return map.get(key);
    }

    @Override
    public void put(K key, V value) {
        map.put(key, value);
    }

    @Override
    public V remove(K key) throws ADTException {
        if (!map.containsKey(key)) {
            throw new KeyNotFoundException();
        }
        return map.remove(key);
    }

    @Override
    public boolean containsKey(K key) {
        for (K k : this.map.keySet()) {
            if (k.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<K> getKeys() {
        return map.keySet();
    }

    @Override
    public List<V> getValues() {
        return map.values().stream().toList();
    }

    @Override
    public Map<K, V> getContent() {
        return map;
    }

    @Override
    public MyIMap<K, V> deepCopy() {
        return new MyMap<>(map);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("SymTable {\n");
        for (Map.Entry<K, V> entry : map.entrySet()) {
            str.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        str.append("}\n");
        return str.toString();
    }
}
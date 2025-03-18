package model.ADT;

import exceptions.ADTException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MyIMap<K, V> {
    K getKey(V value) throws ADTException;
    V getValue(K key) throws ADTException;
    boolean containsKey(K key);
    void put(K key, V value);
    V remove(K key) throws ADTException;
    Set<K> getKeys();
    List<V> getValues();
    Map<K, V> getContent();
    MyIMap<K, V> deepCopy();
    String toString();
}
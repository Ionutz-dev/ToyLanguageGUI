package model.ADT;

import exceptions.ADTException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyBarrierTable implements MyIBarrierTable {
    private MyIMap<Integer, Pair<Integer, List<Integer>>> barrierTable;
    private static int currentAddress = 1;
    private Lock lock = new ReentrantLock();

    public MyBarrierTable() {
        this.barrierTable = new MyMap<>();
    }

    @Override
    public int allocate(int value) {
        lock.lock();
        try {
            barrierTable.put(currentAddress++, new Pair<>(value, new ArrayList<>()));
            return currentAddress - 1;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Pair<Integer, List<Integer>> getValue(int key) throws ADTException {
        lock.lock();
        try {
            return barrierTable.getValue(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void setValue(int key, Pair<Integer, List<Integer>> value) throws ADTException {
        lock.lock();
        try {
            if (!barrierTable.containsKey(key)) {
                throw new ADTException("BarrierTable does not contain key " + key);
            }
            barrierTable.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean containsKey(int key) {
        lock.lock();
        try {
            return barrierTable.containsKey(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public MyIMap<Integer, Pair<Integer, List<Integer>>> getContent() {
        return barrierTable;
    }

    @Override
    public void setContent(MyIMap<Integer, Pair<Integer, List<Integer>>> newContent) {
        lock.lock();
        try {
            barrierTable.getContent().clear();
            barrierTable.getContent().putAll(newContent.getContent());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("BarrierTable {\n");
        for (Map.Entry<Integer, Pair<Integer, List<Integer>>> entry : barrierTable.getContent().entrySet()) {
            str.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        str.append("}\n");
        return str.toString();
    }
}

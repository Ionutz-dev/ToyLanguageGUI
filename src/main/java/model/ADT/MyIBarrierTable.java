package model.ADT;

import exceptions.ADTException;
import javafx.util.Pair;

import java.util.List;

public interface MyIBarrierTable {
    int allocate(int value);
    Pair<Integer, List<Integer>> getValue(int key) throws ADTException;
    void setValue(int key, Pair<Integer, List<Integer>> value) throws ADTException;
    boolean containsKey(int key);
    MyIMap<Integer, Pair<Integer, List<Integer>>> getContent();
    void setContent(MyIMap<Integer, Pair<Integer, List<Integer>>> newContent);
}

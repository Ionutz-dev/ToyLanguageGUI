package model.ADT;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements MyIList<T> {
    private List<T> list;

    public MyList() {
        this.list = new ArrayList<>();
    }

    public MyList(List<T> list) {
        this.list = list;
    }

    @Override
    public void add(T value) {
        list.add(value);
    }

    @Override
    public List<T> getContent() {
        return list;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Output List: [ ");
        for (T t : list) {
            str.append(t).append(" ");
        }
        str.append("]\n");
        return str.toString();
    }
}
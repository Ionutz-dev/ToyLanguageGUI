package model.ADT;

import exceptions.ADTException;
import model.values.IValue;

import java.util.Map;

public class MyHeap implements MyIHeap {
    private MyIMap<Integer, IValue> heap;
    private static int currentAddress = 1;

    public MyHeap() {
        heap = new MyMap<Integer, IValue>();
    }

    @Override
    public int allocate(IValue value) {
        heap.put(currentAddress++, value);
        return currentAddress - 1;
    }

    @Override
    public IValue getValue(int address) throws ADTException {
        return heap.getValue(address);
    }

    @Override
    public void setValue(int address, IValue value) {
        heap.put(address, value);
    }

    @Override
    public boolean containsKey(int address) {
        return heap.containsKey(address);
    }

    @Override
    public MyIMap<Integer, IValue> getContent() {
        return heap;
    }

    @Override
    public void setContent(MyIMap<Integer, IValue> heap) {
        this.heap = heap;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Heap {\n");
        for (Map.Entry<Integer, IValue> entry : heap.getContent().entrySet()) {
            str.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        str.append("}\n");
        return str.toString();
    }
}

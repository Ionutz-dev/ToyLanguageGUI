package model.ADT;

import exceptions.ADTException;
import model.values.IValue;

public interface MyIHeap {
    public int allocate(IValue value);
    public IValue getValue(int address) throws ADTException;
    public void setValue(int address, IValue value);
    public boolean containsKey(int address);
    public MyIMap<Integer, IValue> getContent();
    public void setContent(MyIMap<Integer, IValue> heap);
}

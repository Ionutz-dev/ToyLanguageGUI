package model.ADT;

import exceptions.ADTException;
import exceptions.EmptyStackException;

public interface MyIStack<T> {
    T pop() throws ADTException;
    void push(T value);
    public boolean isEmpty();
    public T top() throws ADTException;
    String toString();
}
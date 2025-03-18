package model.ADT;

//import com.sun.source.tree.EmptyStatementTree;
import exceptions.ADTException;
import exceptions.EmptyStackException;

import java.util.Stack;

public class MyStack<T> implements MyIStack<T> {
    private Stack<T> stack;

    public MyStack() {
        this.stack = new Stack<>();
    }

    public MyStack(Stack<T> stack) {
        this.stack = stack;
    }

    @Override
    public T pop() throws ADTException {
        if (stack.isEmpty()) {
            throw new EmptyStackException();
        }
        return stack.pop();
    }

    @Override
    public void push(T value) {
        stack.push(value);
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public T top() throws ADTException {
        if (stack.isEmpty()) {
            throw new EmptyStackException();
        }
        return stack.peek();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Exec stack:\n");
        for (T t : stack) {
            str.append(t).append("\n");
        }
        return str.toString();
    }
}
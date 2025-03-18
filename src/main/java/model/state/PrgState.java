package model.state;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.ADT.*;
import model.values.*;
import model.statements.*;

import java.io.BufferedReader;

public class PrgState {
    private MyIStack<IStatement> exeStack;
    private MyIMap<String, IValue> symTbl;
    private MyIList<IValue> outList;
    private IStatement originalStatement;
    private MyIMap<StringValue, BufferedReader> fileTable;
    private MyIHeap heap;
    private MyIBarrierTable barrierTable;
    private int id;
    private static int nextId = 1;

    public PrgState(IStatement originalStatement) {
        this.exeStack = new MyStack<IStatement>();
        this.symTbl = new MyMap<String, IValue>();
        this.outList = new MyList<IValue>();
        this.originalStatement = originalStatement;
        this.exeStack.push(originalStatement);
        this.fileTable = new MyMap<StringValue, BufferedReader>();
        this.heap = new MyHeap();
        this.barrierTable = new MyBarrierTable();
        this.id = getNextId();
    }

    public PrgState(MyIStack<IStatement> exeStack, MyIMap<String, IValue> symTbl, MyIList<IValue> outList, MyIHeap heap, MyIMap<StringValue, BufferedReader> fileTable, MyIBarrierTable barrierTable) {
        this.exeStack = exeStack;
        this.symTbl = symTbl;
        this.outList = outList;
        this.heap = heap;
        this.fileTable = fileTable;
        this.barrierTable = barrierTable;
        this.id = getNextId();
    }

    public int getId() {
        return id;
    }

    public MyIStack<IStatement> getExeStack() {
        return exeStack;
    }

    public MyIMap<String, IValue> getSymTbl() {
        return symTbl;
    }

    public MyIList<IValue> getOutList() {
        return outList;
    }

    public IStatement getOriginalStatement() {
        return originalStatement;
    }

    public MyIMap<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    public MyIHeap getHeap() {
        return heap;
    }

    public MyIBarrierTable getBarrierTable() {
        return barrierTable;
    }

    synchronized public int getNextId() {
        return nextId++;
    }

    public void setHeap(MyIHeap heap) {
        this.heap = heap;
    }

    public boolean isNotCompleted() {
        return !exeStack.isEmpty();
    }

    public PrgState oneStep() throws ADTException, StatementException, ExpressionException {
        if (exeStack.isEmpty()) {
            throw new StatementException("Program state stack is empty");
        }
        IStatement currentStatement = exeStack.pop();
        return currentStatement.execute(this);
    }

    public String fileTableToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("File Table {\n");
        for (StringValue path : fileTable.getKeys()) {
            sb.append(path).append("\n");
        }
        sb.append("}\n");
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Id ").append(id).append(" ");
        sb.append(symTbl.toString()).append("\n");
        sb.append(exeStack.toString()).append("\n");

        return sb.toString();
    }
}
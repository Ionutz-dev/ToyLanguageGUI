package model.expressions;

import exceptions.ADTException;
import exceptions.ExpressionException;
import model.ADT.MyIHeap;
import model.ADT.MyIMap;
import model.values.IValue;
import model.types.IType;

public class VariableExpression implements IExpression {
    private String name;

    public VariableExpression(String name) {
        this.name = name;
    }

    @Override
    public IValue eval(MyIMap<String, IValue> symTbl, MyIHeap heap) throws ADTException, ExpressionException {
        if (!symTbl.containsKey(name)) {
            throw new ExpressionException("Variable " + name + " is not defined");
        }
        return symTbl.getValue(name);
    }

    @Override
    public IExpression deepCopy() {
        return new VariableExpression(name);
    }

    @Override
    public IType typeCheck(MyIMap<String, IType> typeEnvironment) throws ADTException  {
        return typeEnvironment.getValue(name);
    }

    @Override
    public String toString() {
        return name;
    }
}

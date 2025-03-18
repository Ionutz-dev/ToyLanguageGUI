package model.expressions;

import model.ADT.MyIHeap;
import model.ADT.MyIMap;
import model.values.IValue;
import model.types.IType;

public class ValueExpression implements IExpression {
    private IValue value;

    public ValueExpression(IValue value) {
        this.value = value;
    }

    @Override
    public IValue eval(MyIMap<String, IValue> symTbl, MyIHeap heap) {
        return value;
    }

    @Override
    public IExpression deepCopy() {
        return new ValueExpression(value.deepCopy());
    }

    @Override
    public IType typeCheck(MyIMap<String, IType> typeEnvironment) {
        return value.getType();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

package model.expressions;

import exceptions.ADTException;
import exceptions.ExpressionException;
import model.ADT.MyIHeap;
import model.ADT.MyIMap;
import model.types.IType;
import model.values.IValue;
import model.values.RefValue;
import model.types.RefType;

public class HeapReadingExpression implements IExpression {
    private IExpression expression;

    public HeapReadingExpression(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public IValue eval(MyIMap<String, IValue> symTbl, MyIHeap heap) throws ADTException, ExpressionException {
        IValue result = expression.eval(symTbl, heap);
        if (!(result instanceof RefValue)) {
            throw new ExpressionException("Value is not a RefValue");
        }

        RefValue refValue = (RefValue) result;
        int address = refValue.getAddress();
        if (!heap.containsKey(address)) {
            throw new ExpressionException("Address " + address + " not found in heap");
        }

        return heap.getValue(address);
    }

    @Override
    public IExpression deepCopy() {
        return new HeapReadingExpression(expression.deepCopy());
    }

    @Override
    public IType typeCheck(MyIMap<String, IType> typeEnvironment) throws ADTException, ExpressionException {
        IType type = expression.typeCheck(typeEnvironment);
        if (!(type instanceof RefType)) {
            throw new ExpressionException("Expression is not a RefType");
        }
        return ((RefType) type).getInnerType();
    }

    @Override
    public String toString() {
        return "rH(" + expression + ")";
    }
}

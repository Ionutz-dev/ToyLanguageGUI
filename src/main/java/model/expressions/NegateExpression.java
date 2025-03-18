package model.expressions;

import exceptions.ADTException;
import exceptions.ExpressionException;
import model.ADT.MyIHeap;
import model.ADT.MyIMap;
import model.types.BoolType;
import model.types.IType;
import model.values.BoolValue;
import model.values.IValue;

public class NegateExpression implements IExpression {
    IExpression expression;

    public NegateExpression(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public IValue eval(MyIMap<String, IValue> symTbl, MyIHeap heap) throws ADTException, ExpressionException {
        IValue value = expression.eval(symTbl, heap);
        if (!value.getType().equals(new BoolType())) {
            throw new ExpressionException("NegateExpression requires a BoolType expression");
        }

        BoolValue boolValue = (BoolValue) value;
        return new BoolValue(!boolValue.getValue());
    }

    @Override
    public IType typeCheck(MyIMap<String, IType> typeEnvironment) throws ExpressionException, ADTException {
        IType type = expression.typeCheck(typeEnvironment);
        if (!type.equals(new BoolType())) {
            throw new ExpressionException("NegateExpression requires a BoolType expression");
        }
        return new BoolType();
    }

    @Override
    public IExpression deepCopy() {
        return new NegateExpression(expression.deepCopy());
    }

    @Override
    public String toString() {
        return "!(" + expression.toString() + ")";
    }
}

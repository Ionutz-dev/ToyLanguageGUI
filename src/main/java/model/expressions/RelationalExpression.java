package model.expressions;

import exceptions.ADTException;
import exceptions.ExpressionException;
import model.ADT.MyIHeap;
import model.ADT.MyIMap;
import model.state.PrgState;
import model.types.BoolType;
import model.types.IType;
import model.types.IntType;
import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;

public class RelationalExpression implements IExpression {
    private IExpression left;
    private IExpression right;
    private RelationalOperator operator;

    public RelationalExpression(IExpression left, IExpression right, RelationalOperator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public IValue eval(MyIMap<String, IValue> symTbl, MyIHeap heap) throws ADTException, ExpressionException {
        IValue leftValue = left.eval(symTbl, heap);
        IValue rightValue = right.eval(symTbl, heap);

        if (!leftValue.getType().equals(new IntType())) {
            throw new ExpressionException(" Value " + leftValue + " is not an Int type");
        }

        if (!rightValue.getType().equals(new IntType())) {
            throw new ExpressionException(" Value " + rightValue + " is not an Int type");
        }

        IntValue leftInt = (IntValue) leftValue;
        IntValue rightInt = (IntValue) rightValue;
        switch (operator) {
            case EQUAL:
                return new BoolValue(leftInt.getValue() == rightInt.getValue());
            case NOT_EQUAL:
                return new BoolValue(leftInt.getValue() != rightInt.getValue());
            case LESS:
                return new BoolValue(leftInt.getValue() < rightInt.getValue());
            case LESS_OR_EQUAL:
                return new BoolValue(leftInt.getValue() <= rightInt.getValue());
            case GREATER:
                return new BoolValue(leftInt.getValue() > rightInt.getValue());
            case GREATER_OR_EQUAL:
                return new BoolValue(leftInt.getValue() >= rightInt.getValue());
            default:
                throw new ExpressionException("Unknown operator");
        }
    }

    @Override
    public IExpression deepCopy() {
        return new RelationalExpression(left.deepCopy(), right.deepCopy(), operator);
    }

    @Override
    public IType typeCheck(MyIMap<String, IType> typeEnvironment) throws ADTException, ExpressionException {
        IType leftType = left.typeCheck(typeEnvironment);
        IType rightType = right.typeCheck(typeEnvironment);

        if (!leftType.equals(new IntType())) {
            throw new ExpressionException("Left " + leftType + "is not an int");
        }
        if (!rightType.equals(new IntType())) {
            throw new ExpressionException("Right " + rightType + "is not an int");
        }

        return new BoolType();
    }

    @Override
    public String toString() {
        return left.toString() + " " + operator + " " + right.toString();
    }
}

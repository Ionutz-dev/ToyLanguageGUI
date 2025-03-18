package model.expressions;


import exceptions.ADTException;
import model.ADT.MyIHeap;
import model.ADT.MyIMap;
import model.types.IntType;
import model.values.IValue;
import model.types.IType;
import exceptions.ExpressionException;
import model.values.IntValue;

public class ArithmeticalExpression implements IExpression {
    private IExpression left;
    private IExpression right;
    private ArithmeticalOperator operator;

    public ArithmeticalExpression(IExpression left, ArithmeticalOperator operator, IExpression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public IValue eval(MyIMap<String, IValue> symTbl, MyIHeap heap) throws ADTException, ExpressionException {
        IValue leftResult = left.eval(symTbl, heap);
        IValue rightResult = right.eval(symTbl, heap);

        if (!leftResult.getType().equals(new IntType())) {
            throw new ExpressionException(" Value " + leftResult + " is not an int");
        }
        if (!rightResult.getType().equals(new IntType())) {
            throw new ExpressionException(" Value " + rightResult + " is not an int");
        }

        IntValue leftInt = (IntValue) leftResult;
        IntValue rightInt = (IntValue) rightResult;
        switch (operator) {
            case PLUS:
                return new IntValue(leftInt.getValue() + rightInt.getValue());
            case MINUS:
                return new IntValue(leftInt.getValue() - rightInt.getValue());
            case MULTIPLY:
                return new IntValue(leftInt.getValue() * rightInt.getValue());
            case DIVIDE:
                if (rightInt.getValue() == 0) {
                    throw new ExpressionException("Division by zero");
                }
                return new IntValue(leftInt.getValue() / rightInt.getValue());
            default:
                throw new ExpressionException("Unknown operator");
        }
    }

    @Override
    public IExpression deepCopy() {
        return new ArithmeticalExpression(left.deepCopy(), operator, right.deepCopy());
    }

    @Override
    public IType typeCheck(MyIMap<String, IType> typeEnvironment) throws ADTException, ExpressionException {
        IType leftType = left.typeCheck(typeEnvironment);
        IType rightType = right.typeCheck(typeEnvironment);

        if (!leftType.equals(new IntType())) {
            throw new ExpressionException("Left operand is not an int");
        }
        if (!rightType.equals(new IntType())) {
            throw new ExpressionException("Right operand is not an int");
        }

        return new IntType();
    }

    @Override
    public String toString() {
        return left.toString() + operator.toString() + right.toString();
    }
}
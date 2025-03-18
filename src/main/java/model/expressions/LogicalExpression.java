package model.expressions;


import exceptions.ADTException;
import exceptions.ExpressionException;
import model.ADT.MyIHeap;
import model.ADT.MyIMap;
import model.types.BoolType;
import model.types.IType;
import model.values.BoolValue;
import model.values.IValue;

public class LogicalExpression implements IExpression {
    private IExpression left;
    private IExpression right;
    private LogicalOperator operator;

    public LogicalExpression(IExpression left, LogicalOperator operator, IExpression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public IValue eval(MyIMap<String, IValue> symTbl, MyIHeap heap) throws ADTException, ExpressionException {
        IValue leftResult = left.eval(symTbl, heap);
        IValue rightResult = right.eval(symTbl, heap);

        if (!leftResult.getType().equals(new BoolType())) {
            throw new ExpressionException("Left " + leftResult.getType() + "is not a bool");
        }
        if (!rightResult.getType().equals(new BoolType())) {
            throw new ExpressionException("Right " + rightResult.getType() + "is not a bool");
        }
        BoolValue rightBool = (BoolValue) rightResult;
        BoolValue leftBool = (BoolValue) leftResult;

        switch (operator) {
            case AND:
                return new BoolValue(leftBool.getValue() && rightBool.getValue());
            case OR:
                return new BoolValue(rightBool.getValue() || leftBool.getValue());
            default:
                throw new ExpressionException("Unknown operator");
        }
    }

    @Override
    public IExpression deepCopy() {
        return new LogicalExpression(left.deepCopy(), operator, right.deepCopy());
    }

    @Override
    public IType typeCheck(MyIMap<String, IType> typeEnvironment) throws ADTException, ExpressionException {
        IType leftType = left.typeCheck(typeEnvironment);
        IType rightType = right.typeCheck(typeEnvironment);

        if (!leftType.equals(new BoolType())) {
            throw new ExpressionException("Left " + leftType + "is not a bool");
        }
        if (!rightType.equals(new BoolType())) {
            throw new ExpressionException("Right " + rightType + "is not a bool");
        }

        return new BoolType();
    }

    @Override
    public String toString() {
        return left.toString() + operator.toString() + right.toString();
    }
}
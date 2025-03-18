package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.ADT.MyIMap;
import model.ADT.MyIHeap;
import model.expressions.IExpression;
import model.state.PrgState;
import model.types.IType;
import model.types.RefType;
import model.values.IValue;
import model.values.RefValue;

public class HeapWritingStatement implements IStatement {
    private String variable;
    private IExpression expression;

    public HeapWritingStatement(String variable, IExpression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws ADTException, StatementException, ExpressionException {
        MyIMap<String, IValue> symTable = state.getSymTbl();
        MyIHeap heap = state.getHeap();

        if (!symTable.containsKey(variable)) {
            throw new StatementException("Variable " + variable + " not found");
        }

        IValue value = symTable.getValue(variable);
        if (!(value instanceof RefValue)) {
            throw new StatementException("Variable " + variable + " is not a reference");
        }

        RefValue refValue = (RefValue) value;
        int address = refValue.getAddress();
        if (!heap.containsKey(address)) {
            throw new ExpressionException("Address " + address + " not found in heap");
        }

        IValue result = expression.eval(symTable, heap);
        if (!result.getType().equals(refValue.getLocationType())) {
            throw new StatementException("The expression isn't match with the type of the variable");
        }

        heap.setValue(address, result);
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new HeapWritingStatement(variable, expression.deepCopy());
    }

    @Override
    public MyIMap<String, IType> typeCheck(MyIMap<String, IType> typeEnvironment) throws StatementException, ADTException, ExpressionException {
        IType variableType = typeEnvironment.getValue(variable);
        IType expressionType = expression.typeCheck(typeEnvironment);
        if (!variableType.equals(new RefType(expressionType))) {
            throw new StatementException("Variable " + variable + " is not of type " + expressionType);
        }
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "wH(" + variable + ", " + expression.toString() + ")";
    }
}

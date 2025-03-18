package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.ADT.MyIHeap;
import model.ADT.MyIMap;
import model.expressions.IExpression;
import model.state.PrgState;
import model.types.IType;
import model.types.RefType;
import model.values.IValue;
import model.values.RefValue;

public class HeapAllocationStatement implements IStatement {
    private String variable;
    private IExpression expression;

    public HeapAllocationStatement(String variable, IExpression expression) {
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

        IValue result = expression.eval(symTable, heap);
        if (!result.getType().equals(((RefValue) value).getLocationType())) {
            throw new StatementException("The expression isn't match with the type of the variable");
        }

        int address = heap.allocate(result);
        symTable.put(variable, new RefValue(address, ((RefValue) value).getLocationType()));
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new HeapAllocationStatement(variable, expression.deepCopy());
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
        return "new(" + variable + ", " + expression + ")";
    }
}

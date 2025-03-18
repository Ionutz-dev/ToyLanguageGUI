package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.ADT.MyIMap;
import model.expressions.*;
import model.state.PrgState;
import model.values.IValue;
import model.types.IType;

public class AssignmentStatement implements IStatement {
    private String variable;
    private IExpression expression;

    public AssignmentStatement(String variable, IExpression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws ADTException, StatementException, ExpressionException {
        if (!state.getSymTbl().containsKey(variable)) {
            throw new StatementException("Variable " + variable + " not found");
        }

        IValue val = expression.eval(state.getSymTbl(), state.getHeap());
        if (!val.getType().equals(state.getSymTbl().getValue(variable).getType())) {
            throw new StatementException("The types do not correspond");
        }

        state.getSymTbl().put(variable, val);
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new AssignmentStatement(variable, expression.deepCopy());
    }

    @Override
    public MyIMap<String, IType> typeCheck(MyIMap<String, IType> typeEnvironment) throws StatementException, ADTException, ExpressionException {
        IType variableType = typeEnvironment.getValue(variable);
        IType expressionType = expression.typeCheck(typeEnvironment);
        if (!variableType.equals(expressionType)) {
            throw new StatementException("Assignment: right hand side and left hand side have different types");
        }
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return variable + " = " + expression;
    }
}

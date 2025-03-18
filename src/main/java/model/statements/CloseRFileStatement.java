package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.ADT.MyIMap;
import model.expressions.IExpression;
import model.state.PrgState;
import model.types.IType;
import model.types.StringType;
import model.values.IValue;
import model.values.StringValue;

import java.io.IOException;

public class CloseRFileStatement implements IStatement {
    private IExpression expression;

    public CloseRFileStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws ADTException, ExpressionException, StatementException {
        IValue result = expression.eval(state.getSymTbl(), state.getHeap());
        if (!result.getType().equals(new StringType())) {
            throw new StatementException("The expression must be a string");
        }

        StringValue filename = (StringValue) result;
        if (!state.getFileTable().containsKey(filename)) {
            throw new StatementException("File not open");
        }

        try {
            state.getFileTable().getValue(filename).close();
            state.getFileTable().remove(filename);
        } catch (IOException e) {
            throw new StatementException("Error closing file: " + e.getMessage());
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new CloseRFileStatement(expression.deepCopy());
    }

    @Override
    public MyIMap<String, IType> typeCheck(MyIMap<String, IType> typeEnvironment) throws StatementException, ADTException, ExpressionException {
        IType expressionType = expression.typeCheck(typeEnvironment);
        if (!expressionType.equals(new StringType())) {
            throw new StatementException("The expression must be a string");
        }
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "closeRFile(" + expression.toString() + ")";
    }
}

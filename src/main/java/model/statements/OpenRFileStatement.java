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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OpenRFileStatement implements IStatement {
    private IExpression expression;

    public OpenRFileStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws ADTException, ExpressionException, StatementException {
        IValue result = expression.eval(state.getSymTbl(), state.getHeap());
        if (!result.getType().equals(new StringType())) {
            throw new StatementException("The expression must be a string");
        }

        StringValue filename = (StringValue) result;
        if (state.getFileTable().containsKey(filename)) {
            throw new StatementException("File already open");
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename.getValue()));
            state.getFileTable().put(filename, reader);
        } catch (IOException e) {
            throw new StatementException("Error opening file: " + e.getMessage());
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new OpenRFileStatement(expression);
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
        return "openRFile(" + expression + ")";
    }
}

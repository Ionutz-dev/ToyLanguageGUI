package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.ADT.MyIHeap;
import model.ADT.MyIMap;
import model.ADT.FileTable;
import model.expressions.IExpression;
import model.state.PrgState;
import model.types.IType;
import model.types.IntType;
import model.types.StringType;
import model.values.IValue;
import model.values.IntValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStatement implements IStatement {
    private IExpression expression;
    private String variable;

    public ReadFileStatement(IExpression expression, String variable) {
        this.expression = expression;
        this.variable = variable;
    }

    @Override
    public PrgState execute(PrgState state) throws ADTException, StatementException, ExpressionException {
        MyIMap<String, IValue> symTable = state.getSymTbl();
        MyIHeap heap = state.getHeap();
        if (!symTable.containsKey(variable)) {
            throw new StatementException("Variable not found");
        }

        IValue value = symTable.getValue(variable);
        if (!value.getType().equals(new IntType())) {
            throw new StatementException("Variable '" + variable + "' is not a number");
        }

        IValue result = expression.eval(symTable, heap);
        if (!result.getType().equals(new StringType())) {
            throw new StatementException("The expression must be a string");
        }

        StringValue fileName = (StringValue) result;
        MyIMap<StringValue, BufferedReader> fileTable = state.getFileTable();
        if (!fileTable.containsKey(fileName)) {
            throw new StatementException("The file isn't open for reading");
        }

        BufferedReader reader = fileTable.getValue(fileName);
        try {
            String line = reader.readLine();
            int num;
            if (line == null) {
                num = 0;
            } else {
                num = Integer.parseInt(line);
            }
            IntValue intValue = new IntValue(num);
            symTable.put(variable, intValue);
        } catch (IOException e) {
            throw new StatementException(e.getMessage());
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new ReadFileStatement(expression.deepCopy(), variable);
    }

    @Override
    public MyIMap<String, IType> typeCheck(MyIMap<String, IType> typeEnvironment) throws StatementException, ADTException, ExpressionException {
        IType expressionType = expression.typeCheck(typeEnvironment);
        if (!expressionType.equals(new StringType())) {
            throw new StatementException("The expression must be a string");
        }
        IType variableType = typeEnvironment.getValue(variable);
        if (!variableType.equals(new IntType())) {
            throw new StatementException("Variable '" + variable + "' is not a number");
        }
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "readFile(" + expression + ", " + variable + ")";
    }
}

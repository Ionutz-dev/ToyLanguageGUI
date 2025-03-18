package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.ADT.MyIMap;
import model.expressions.IExpression;
import model.expressions.NegateExpression;
import model.state.PrgState;
import model.types.BoolType;
import model.types.IType;
import model.values.IValue;

public class RepeatStatement implements IStatement {
    IStatement statement;
    IExpression expression;

    public RepeatStatement(IStatement statement, IExpression expression) {
        this.statement = statement;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws ADTException, StatementException, ExpressionException {
        IStatement transformedStatement = new CompoundStatement(
                statement,
                new WhileStatement(
                        statement,
                        new NegateExpression(expression)
                )
        );
        state.getExeStack().push(transformedStatement);

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new RepeatStatement(statement.deepCopy(), expression.deepCopy());
    }

    @Override
    public MyIMap<String, IType> typeCheck(MyIMap<String, IType> typeEnvironment) throws ADTException, StatementException, ExpressionException {
        IType expressionType = expression.typeCheck(typeEnvironment);
        if (!expressionType.equals(new BoolType())) {
            throw new StatementException("The condition of the repeat is not a boolean");
        }
        statement.typeCheck(typeEnvironment.deepCopy());
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "repeat { " + statement.toString() + " } until (" + expression.toString() + ")";
    }
}

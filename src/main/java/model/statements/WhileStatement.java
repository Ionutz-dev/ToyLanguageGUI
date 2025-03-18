package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.ADT.MyIHeap;
import model.ADT.MyIMap;
import model.expressions.IExpression;
import model.state.PrgState;
import model.types.IType;
import model.types.BoolType;
import model.values.IValue;
import model.values.BoolValue;

public class WhileStatement implements IStatement {
    private IStatement statement;
    private IExpression expression;

    public WhileStatement(IStatement statement, IExpression expression) {
        this.statement = statement;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws ADTException, StatementException, ExpressionException {
        MyIMap<String, IValue> symTable = state.getSymTbl();
        MyIHeap heap = state.getHeap();

        IValue result = expression.eval(symTable, heap);
        if (!result.getType().equals(new BoolType())) {
            throw new StatementException("Condition expression does not evaluate to BoolType");
        }

        BoolValue boolValue = (BoolValue) result;
        if (boolValue.getValue()) {
            state.getExeStack().push(this);
            state.getExeStack().push(statement);
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new WhileStatement(statement.deepCopy(), expression.deepCopy());
    }

    @Override
    public MyIMap<String, IType> typeCheck(MyIMap<String, IType> typeEnvironment) throws StatementException, ADTException, ExpressionException {
        IType expressionType = expression.typeCheck(typeEnvironment);
        if (!expressionType.equals(new BoolType())) {
            throw new StatementException("The condition of the while is not a boolean");
        }
        statement.typeCheck(typeEnvironment.deepCopy());
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "while (" + expression.toString() + ") { " + statement.toString() + " }";
    }
}

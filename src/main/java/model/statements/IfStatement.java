package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.ADT.MyIMap;
import model.expressions.IExpression;
import model.state.PrgState;
import model.types.IType;
import model.types.BoolType;
import model.values.*;

public class IfStatement implements IStatement {
    private IExpression condition;
    private IStatement thenStatement;
    private IStatement elseStatement;

    public IfStatement(IExpression condition, IStatement thenStatement, IStatement elseStatement) {
        this.condition = condition;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public PrgState execute(PrgState state) throws ADTException, StatementException, ExpressionException {
        IValue val = condition.eval(state.getSymTbl(), state.getHeap());
        if (!val.getType().equals(new BoolType())) {
            throw new StatementException("Condition expression does not evaluate to BoolType");
        }

        if (((BoolValue) val).getValue()) {
            state.getExeStack().push(thenStatement);
        } else {
            state.getExeStack().push(elseStatement);
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new IfStatement(condition.deepCopy(), thenStatement.deepCopy(), elseStatement.deepCopy());
    }

    @Override
    public MyIMap<String, IType> typeCheck(MyIMap<String, IType> typeEnvironment) throws StatementException, ADTException, ExpressionException {
        IType expressionType = condition.typeCheck(typeEnvironment);
        if (!expressionType.equals(new BoolType())) {
            throw new StatementException("The condition of the if is not a boolean");
        }
        thenStatement.typeCheck(typeEnvironment.deepCopy());
        elseStatement.typeCheck(typeEnvironment.deepCopy());
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "if(" + condition + "){\n" + thenStatement + "}" + "\nelse{\n" + elseStatement + "\n}";
    }
}
package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.ADT.MyBarrierTable;
import model.ADT.MyIBarrierTable;
import model.ADT.MyIMap;
import model.expressions.IExpression;
import model.state.PrgState;
import model.types.IType;
import model.types.IntType;
import model.values.IValue;
import model.values.IntValue;

public class NewBarrierStatement implements IStatement {
    private String variable;
    private IExpression expression;

    public NewBarrierStatement(String variable, IExpression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws ADTException, StatementException, ExpressionException {
        IValue value = expression.eval(state.getSymTbl(), state.getHeap());
        if (!value.getType().equals(new IntType())) {
            throw new StatementException("Expression must evaluate to an integer");
        }
        if (!(state.getSymTbl().containsKey(variable) && state.getSymTbl().getValue(variable).getType().equals(new IntType()))) {
            throw new StatementException("Variable " + variable + " must be an integer");
        }

        int number = ((IntValue) value).getValue();
        MyIBarrierTable barrierTable =  state.getBarrierTable();
        int newLocation = barrierTable.allocate(number);

        state.getSymTbl().put(variable, new IntValue(newLocation));

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new NewBarrierStatement(variable, expression.deepCopy());
    }

    @Override
    public MyIMap<String, IType> typeCheck(MyIMap<String, IType> typeEnvironment) throws ADTException, StatementException, ExpressionException {
        IType variableType = typeEnvironment.getValue(variable);
        IType expressionType = expression.typeCheck(typeEnvironment);
        if (!expressionType.equals(new IntType())) {
            throw new StatementException("The expression must be an integer");
        }
        if (!variableType.equals(new IntType())) {
            throw new StatementException("The variable must be an integer");
        }
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "newBarrier(" + variable + ", " + expression + ")";
    }
}

package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import javafx.util.Pair;
import model.ADT.MyIBarrierTable;
import model.ADT.MyIMap;
import model.state.PrgState;
import model.types.IType;
import model.types.IntType;
import model.values.IntValue;

import java.util.List;

public class AwaitStatement implements IStatement {
    private String variable;

    public AwaitStatement(String variable) {
        this.variable = variable;
    }

    @Override
    public PrgState execute(PrgState state) throws ADTException, StatementException, ExpressionException {
        if (!state.getSymTbl().containsKey(variable) || !(state.getSymTbl().getValue(variable).getType().equals(new IntType()))) {
            throw new StatementException("Variable must be of type int");
        }

        int foundIndex = ((IntValue) state.getSymTbl().getValue(variable)).getValue();
        MyIBarrierTable barrierTable = state.getBarrierTable();

        if (!barrierTable.containsKey(foundIndex)) {
            throw new StatementException("Barrier index not found");
        }

        Pair<Integer, List<Integer>> barrierEntry = barrierTable.getValue(foundIndex);
        int N1 = barrierEntry.getKey();
        List<Integer> List1 = barrierEntry.getValue();

        if (N1 > List1.size()) {
            if (!List1.contains(state.getId())) {
                List1.add(state.getId());
            }
            state.getExeStack().push(this);
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new AwaitStatement(variable);
    }

    @Override
    public MyIMap<String, IType> typeCheck(MyIMap<String, IType> typeEnvironment) throws ADTException, StatementException, ExpressionException {
        IType variableType = typeEnvironment.getValue(variable);
        if (!variableType.equals(new IntType())) {
            throw new StatementException("Variable must be of type int");
        }
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "await(" + variable + ")";
    }
}

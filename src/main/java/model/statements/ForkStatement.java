package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.ADT.MyIStack;
import model.ADT.MyStack;
import model.ADT.MyIMap;
import model.types.IType;
import model.state.PrgState;

public class ForkStatement implements IStatement {
    private IStatement statement;

    public ForkStatement(IStatement statement) {
        this.statement = statement;
    }

    @Override
    public PrgState execute(PrgState state) {
        MyIStack<IStatement> newExeStack = new MyStack<>();
        newExeStack.push(statement);
        return new PrgState(newExeStack, state.getSymTbl().deepCopy(), state.getOutList(), state.getHeap(), state.getFileTable(), state.getBarrierTable());
    }

    @Override
    public IStatement deepCopy(){
        return new ForkStatement(statement.deepCopy());
    }

    @Override
    public MyIMap<String, IType> typeCheck(MyIMap<String, IType> typeEnvironment) throws StatementException, ADTException, ExpressionException {
        statement.typeCheck(typeEnvironment.deepCopy());
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "fork(" + statement.toString() + ")";
    }
}

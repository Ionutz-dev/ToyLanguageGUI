package model.statements;

import model.ADT.MyIMap;
import model.types.IType;
import model.state.PrgState;

public class NopStatement implements IStatement {
    @Override
    public PrgState execute(PrgState state) {
        return null;
    } 

    @Override
    public IStatement deepCopy() {
        return new NopStatement();
    }

    @Override
    public MyIMap<String, IType> typeCheck(MyIMap<String, IType> typeEnvironment) {
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "nop";
    }
}

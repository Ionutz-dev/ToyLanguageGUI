package model.statements;

import model.state.PrgState;
import model.ADT.MyIMap;
import model.types.IType;
import exceptions.StatementException;

public class VarDeclStatement implements IStatement {
    String name;
    IType type;

    public VarDeclStatement(String name, IType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        if (state.getSymTbl().containsKey(name)) {
            throw new StatementException("Variable " + name + " already exists");
        }
        state.getSymTbl().put(name, type.getDefaultValue());
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new VarDeclStatement(name, type);
    }

    @Override
    public MyIMap<String, IType> typeCheck(MyIMap<String, IType> typeEnvironment) {
        typeEnvironment.put(name, type);
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return type + " " + name;
    }
}

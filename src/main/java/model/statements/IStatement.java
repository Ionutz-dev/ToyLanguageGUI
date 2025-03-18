package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.ADT.MyIMap;
import model.state.PrgState;
import model.types.IType;

public interface IStatement {
    PrgState execute(PrgState p) throws StatementException, ADTException, ExpressionException;
    IStatement deepCopy();
    MyIMap<String, IType> typeCheck(MyIMap<String, IType> typeEnvironment) throws ADTException, StatementException, ExpressionException;
}
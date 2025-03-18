package repository;

import model.state.PrgState;
import exceptions.RepositoryException;

import java.util.List;

public interface IRepository {
    void add(PrgState state);
    List<PrgState> getPrgList();
    void setPrgList(List<PrgState> prgList);
    void logPrgStateExec(List<PrgState> prgList) throws RepositoryException;
}

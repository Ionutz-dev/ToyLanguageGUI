package repository;

import exceptions.RepositoryException;
import model.ADT.MyIBarrierTable;
import model.ADT.MyIHeap;
import model.ADT.MyIList;
import model.state.PrgState;
import model.values.IValue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private List<PrgState> prgList;
    private String logFilePath;

    public Repository(String logFilePath) {
        this.prgList = new ArrayList<>();
        this.logFilePath = logFilePath;
    }

    @Override
    public void add(PrgState prg) {
       prgList.add(prg);
    }

    @Override
    public List<PrgState> getPrgList() {
        return prgList;
    }

    @Override
    public void setPrgList(List<PrgState> prgList) {
        this.prgList = prgList;
    }

    @Override
    public void logPrgStateExec(List<PrgState> prgList) throws RepositoryException {
        try {
            PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
            StringBuilder sb = new StringBuilder();

            sb.append("---- Program States ----\n\n");

            for (PrgState prg : prgList) {
                sb.append(prg.toString());
            }

            if (!prgList.isEmpty()) {
                MyIHeap heap = prgList.getFirst().getHeap();
                sb.append(heap.toString()).append("\n");

                MyIList<IValue> outList = prgList.getFirst().getOutList();
                sb.append(outList.toString()).append("\n");

                MyIBarrierTable barrierTable = prgList.getFirst().getBarrierTable();
                sb.append(barrierTable.toString()).append("\n");
            }

            logFile.println(sb);
            logFile.close();
        } catch (IOException e) {
            throw new RepositoryException(e.getMessage());
        }
    }
}

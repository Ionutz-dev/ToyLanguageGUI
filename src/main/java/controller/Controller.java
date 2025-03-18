package controller;

import exceptions.*;
import model.ADT.*;
import model.state.PrgState;
import model.values.IValue;
import model.values.RefValue;
import repository.IRepository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller {
    private IRepository repository;
    ExecutorService executor;
    private boolean displayFlag;

    public Controller(IRepository repository, ExecutorService executor, boolean displayFlag) {
        this.repository = repository;
        this.executor = executor;
        this.displayFlag = displayFlag;
    }

    public IRepository getRepository() {
        return repository;
    }

    private void displayPrgStateExec(List<PrgState> prgList) {
        if (displayFlag) {
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

            System.out.println(sb.toString());
        }
    }

    private MyIMap<Integer, IValue> safeGarbageCollector(List<Integer> symTableAddr, List<Integer> heapAddr, MyIMap<Integer, IValue> heap) throws ADTException {
        MyIMap<Integer, IValue> newHeap = new MyMap<>();
        symTableAddr.addAll(heapAddr);
        for (Integer address : heap.getKeys()) {
            if (symTableAddr.contains(address)) {
                newHeap.put(address, heap.getValue(address));
            }
        }
        return newHeap;
    }

    private List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues) {
        return symTableValues.stream()
                .filter(val -> val instanceof RefValue)
                .map(val -> ((RefValue) val).getAddress())
                .collect(Collectors.toList());
    }

    private List<Integer> getAddrFromHeap(Collection<IValue> heapValues) {
        return heapValues.stream()
                .filter(val -> val instanceof RefValue)
                .map(val -> ((RefValue) val).getAddress())
                .collect(Collectors.toList());
    }

    private List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream()
                .filter(PrgState::isNotCompleted)
                .toList();
    }

    public void oneStepForAllPrg(List<PrgState> prgList) {
        try {
            repository.logPrgStateExec(prgList);

            List<Callable<PrgState>> callables = prgList.stream()
                    .map((PrgState state) -> (Callable<PrgState>) state::oneStep)
                    .toList();

            executor.invokeAll(callables).stream().map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .forEach(state -> repository.add(state));

            repository.logPrgStateExec(prgList);
        } catch (RepositoryException | InterruptedException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    public void allStep() {
        List<PrgState> prgList = removeCompletedPrg(repository.getPrgList());

        while (!prgList.isEmpty()) {
            oneStepForAllPrg(prgList);

            List<Integer> addrFromSymTbl = prgList.stream().flatMap(prg ->
                getAddrFromSymTable(prg.getSymTbl().getContent().values()).stream()
            ).collect(Collectors.toList());
            MyIHeap heap = prgList.getFirst().getHeap();
            try {
                heap.setContent(safeGarbageCollector(addrFromSymTbl, getAddrFromHeap(heap.getContent().getValues()), heap.getContent()));
            } catch (ADTException e) {
                throw new ControllerException(e.getMessage());
            }

            displayPrgStateExec(prgList);
            prgList = removeCompletedPrg(repository.getPrgList());
        }
        if (!displayFlag) {
            displayPrgStateExec(prgList);
        }

        executor.shutdownNow();
        repository.setPrgList(prgList);
    }
}

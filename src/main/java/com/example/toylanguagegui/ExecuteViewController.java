package com.example.toylanguagegui;

import controller.Controller;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.ADT.*;
import model.state.PrgState;
import model.statements.IStatement;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExecuteViewController {
    @FXML
    private TextField prgStateCount;

    @FXML
    private TableView<List<String>> heapTable;
    @FXML
    private TableColumn<List<String>, String> heapAddressCol;
    @FXML
    private TableColumn<List<String>, String> heapValueCol;

    @FXML
    private ListView<String> outList;

    @FXML
    private ListView<String> fileTable;

    @FXML
    private ListView<Integer> prgStateIdList;

    @FXML
    private TableView<List<String>> symTable;
    @FXML
    private TableColumn<List<String>, String> symVarNameCol;
    @FXML
    private TableColumn<List<String>, String> symValueCol;

    @FXML
    private ListView<String> exeStack;

    @FXML
    private TableView<List<String>> barrierTable;
    @FXML
    private TableColumn<List<String>, String> barrierIndexCol;
    @FXML
    private TableColumn<List<String>, String> barrierValueCol;
    @FXML
    private TableColumn<List<String>, String> barrierListCol;

    @FXML
    private Button runOneStep;

    private Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
        initialize();
        updateView();
    }

    private void initialize() {
        heapAddressCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getFirst()));
        heapValueCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get(1)));

        symVarNameCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getFirst()));
        symValueCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get(1)));

        barrierIndexCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getFirst()));
        barrierValueCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get(1)));
        barrierListCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get(2)));

        prgStateIdList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateDetailsForSelectedPrgState());
    }

    private void updateView() {
        Integer selectedId = prgStateIdList.getSelectionModel().getSelectedItem();

        List<PrgState> prgStates = controller.getRepository().getPrgList();
        prgStateCount.setText(String.valueOf(prgStates.size()));

        prgStateIdList.setItems(FXCollections.observableList(
                prgStates.stream().map(PrgState::getId).collect(Collectors.toList())
        ));

        if (selectedId != null && prgStates.stream().anyMatch(prg -> prg.getId() == selectedId)) {
            prgStateIdList.getSelectionModel().select(selectedId);
        } else if (!prgStates.isEmpty()) {
            prgStateIdList.getSelectionModel().selectFirst();
        }

        updateDetailsForSelectedPrgState();
    }

    private void updateDetailsForSelectedPrgState() {
        PrgState selectedPrgState = getSelectedPrgState();
        if (selectedPrgState == null) return;

        MyIHeap heap = selectedPrgState.getHeap();
        heapTable.setItems(FXCollections.observableList(
                heap.getContent().getContent().entrySet().stream()
                        .map(entry -> List.of(String.valueOf(entry.getKey()), entry.getValue().toString()))
                        .collect(Collectors.toList())
        ));

        MyIList<IValue> out = selectedPrgState.getOutList();
        outList.setItems(FXCollections.observableList(
                out.getContent().stream().map(IValue::toString).collect(Collectors.toList())
        ));

        MyIMap<StringValue, BufferedReader> fileTableContent = selectedPrgState.getFileTable();
        fileTable.setItems(FXCollections.observableList(
                fileTableContent.getKeys().stream().map(StringValue::toString).collect(Collectors.toList())
        ));

        MyIStack<IStatement> exeStackContent = selectedPrgState.getExeStack();
        exeStack.setItems(FXCollections.observableList(
                exeStackContent.toString().lines().collect(Collectors.toList())
        ));

        MyIMap<String, IValue> symTableContent = selectedPrgState.getSymTbl();
        symTable.setItems(FXCollections.observableList(
                symTableContent.getContent().entrySet().stream()
                        .map(entry -> List.of(entry.getKey(), entry.getValue().toString()))
                        .collect(Collectors.toList())
        ));

        MyIBarrierTable barrierTableContent = selectedPrgState.getBarrierTable();
        barrierTable.setItems(FXCollections.observableList(
                barrierTableContent.getContent().getContent().entrySet().stream()
                        .map(entry -> List.of(
                                String.valueOf(entry.getKey()),
                                String.valueOf(entry.getValue().getKey()),
                                entry.getValue().getValue().toString()
                        ))
                        .collect(Collectors.toList())
        ));
    }

    private PrgState getSelectedPrgState() {
        Integer selectedId = prgStateIdList.getSelectionModel().getSelectedItem();
        if (selectedId == null) return null;

        return controller.getRepository().getPrgList().stream()
                .filter(prgState -> prgState.getId() == selectedId)
                .findFirst()
                .orElse(null);
    }

    @FXML
    private void onRunOneStep() {
        try {
            List<PrgState> prgStates = new ArrayList<>(controller.getRepository().getPrgList());

            // Separate completed states (those with empty execution stacks)
            List<PrgState> nonCompletedStates = prgStates.stream()
                    .filter(prgState -> !prgState.getExeStack().isEmpty())
                    .collect(Collectors.toList());

            // If all program states are completed, show an error and stop execution
            if (nonCompletedStates.isEmpty()) {
                showError("All program states have completed execution");
                return;
            }

            // Execute one step for all non-completed program states
            controller.oneStepForAllPrg(nonCompletedStates);

            updateView();
        } catch (Exception e) {
            showError("Error during execution: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}

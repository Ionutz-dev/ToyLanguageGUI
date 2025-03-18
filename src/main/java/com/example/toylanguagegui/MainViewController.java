package com.example.toylanguagegui;

import controller.Controller;
import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.ADT.MyMap;
import model.expressions.*;
import model.state.PrgState;
import model.statements.*;
import model.types.IntType;
import model.types.RefType;
import model.types.StringType;
import model.values.IntValue;
import model.values.StringValue;
import repository.IRepository;
import repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static model.expressions.ArithmeticalOperator.MINUS;
import static model.expressions.ArithmeticalOperator.PLUS;
import static model.expressions.RelationalOperator.GREATER;

public class MainViewController {
    @FXML
    public  ListView<IStatement> prgStateList;

    @FXML
    public Button runBtn;

    @FXML
    public void initialize() {
        prgStateList.getItems().addAll(getStatements());
        prgStateList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private List<IStatement> getStatements() {
        List<IStatement> statements = new ArrayList<>();

        try {
            IStatement ex1 = new CompoundStatement(
                    new VarDeclStatement("varf", new StringType()),
                    new CompoundStatement(
                            new AssignmentStatement("varf", new ValueExpression(new StringValue("test.in"))),
                            new CompoundStatement(
                                    new OpenRFileStatement(new VariableExpression("varf")),
                                    new CompoundStatement(
                                            new VarDeclStatement("varc", new IntType()),
                                            new CompoundStatement(
                                                    new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                    new CompoundStatement(
                                                            new PrintStatement(new VariableExpression("varc")),
                                                            new CompoundStatement(
                                                                    // new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                                    new ReadFileStatement(new ValueExpression(new StringValue("test.in")), "varc"),
                                                                    new CompoundStatement(
                                                                            new PrintStatement(new VariableExpression("varc")),
                                                                            new CloseRFileStatement(new VariableExpression("varf"))))))))));

            ex1.typeCheck(new MyMap<>());
            statements.add(ex1);
        } catch (ADTException | StatementException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
        }

        try {
            // Example 2: int a; a = 5; int b; b = a + 10; print(b);
            IStatement ex2 = new CompoundStatement(
                    new VarDeclStatement("a", new IntType()),
                    new CompoundStatement(
                            new AssignmentStatement("a", new ValueExpression(new IntValue(5))),
                            new CompoundStatement(
                                    new VarDeclStatement("b", new IntType()),
                                    new CompoundStatement(
                                            new AssignmentStatement("b", new ArithmeticalExpression(new VariableExpression("a"), PLUS, new ValueExpression(new IntValue(10)))),
                                            new PrintStatement(new VariableExpression("b"))))));
            ex2.typeCheck(new MyMap<>());
            statements.add(ex2);
        } catch (ADTException | StatementException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
        }

        try {
            // Example 3: int x; x = 20; int y; if (x > 10) then y = 1 else y = 0; print(y);
            IStatement ex3 = new CompoundStatement(
                    new VarDeclStatement("x", new IntType()),
                    new CompoundStatement(
                            new AssignmentStatement("x", new ValueExpression(new IntValue(20))),
                            new CompoundStatement(
                                    new VarDeclStatement("y", new IntType()),
                                    new CompoundStatement(
                                            new IfStatement(new RelationalExpression(new VariableExpression("x"), new ValueExpression(new IntValue(10)), GREATER),
                                                    new AssignmentStatement("y", new ValueExpression(new IntValue(1))),
                                                    new AssignmentStatement("y", new ValueExpression(new IntValue(0)))),
                                            new PrintStatement(new VariableExpression("y"))))));

            ex3.typeCheck(new MyMap<>());
            statements.add(ex3);
        } catch (ADTException | StatementException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
        }

        try {
            // New Examples
            IStatement ex4 = new CompoundStatement(
                    new VarDeclStatement("v", new RefType(new IntType())),
                    new CompoundStatement(
                            new HeapAllocationStatement("v", new ValueExpression(new IntValue(20))),
                            new PrintStatement(new HeapReadingExpression(new VariableExpression("v")))
                    )
            );

            ex4.typeCheck(new MyMap<>());
            statements.add(ex4);
        } catch (ADTException | StatementException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
        }

        try {
            IStatement ex5 = new CompoundStatement(
                    new VarDeclStatement("a", new RefType(new IntType())),
                    new CompoundStatement(
                            new HeapAllocationStatement("a", new ValueExpression(new IntValue(20))),
                            new CompoundStatement(
                                    new VarDeclStatement("b", new RefType(new RefType(new IntType()))),
                                    new CompoundStatement(
                                            new HeapAllocationStatement("b", new VariableExpression("a")),
                                            new PrintStatement(new HeapReadingExpression(new HeapReadingExpression(new VariableExpression("b"))))
                                    )
                            )
                    )
            );

            ex5.typeCheck(new MyMap<>());
            statements.add(ex5);
        } catch (ADTException | StatementException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
        }

        try {
            IStatement ex6 = new CompoundStatement(
                    new VarDeclStatement("v", new RefType(new IntType())),
                    new CompoundStatement(
                            new HeapAllocationStatement("v", new ValueExpression(new IntValue(20))),
                            new CompoundStatement(
                                    new PrintStatement(new HeapReadingExpression(new VariableExpression("v"))),
                                    new CompoundStatement(
                                            new HeapWritingStatement("v", new ValueExpression(new IntValue(30))),
                                            new PrintStatement(new ArithmeticalExpression(
                                                    new HeapReadingExpression(new VariableExpression("v")),
                                                    PLUS,
                                                    new ValueExpression(new IntValue(5))
                                            ))
                                    )
                            )
                    )
            );

            ex6.typeCheck(new MyMap<>());
            statements.add(ex6);
        } catch (ADTException | StatementException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
        }


        try {
            IStatement ex7 = new CompoundStatement(
                    new VarDeclStatement("v1", new RefType(new IntType())),
                    new CompoundStatement(
                            new HeapAllocationStatement("v1", new ValueExpression(new IntValue(20))),
                            new CompoundStatement(
                                    new VarDeclStatement("v2", new RefType(new IntType())),
                                    new CompoundStatement(
                                            new HeapAllocationStatement("v2", new ValueExpression(new IntValue(30))),
                                            new CompoundStatement(
                                                    new VarDeclStatement("v3", new RefType(new RefType(new IntType()))),
                                                    new CompoundStatement(
                                                            new HeapAllocationStatement("v3", new VariableExpression("v2")),
                                                            new CompoundStatement(
                                                                    new HeapWritingStatement("v1", new ValueExpression(new IntValue(50))),
                                                                    new PrintStatement(new HeapReadingExpression(new VariableExpression("v1")))
                                                            )
                                                    )
                                            )
                                    )
                            )
                    )
            );

            ex7.typeCheck(new MyMap<>());
            statements.add(ex7);
        } catch (ADTException | StatementException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
        }

        try {
            IStatement ex8 = new CompoundStatement(
                    new VarDeclStatement("v", new IntType()),
                    new CompoundStatement(
                            new AssignmentStatement("v", new ValueExpression(new IntValue(5))),
                            new CompoundStatement(
                                    new WhileStatement(
                                            new CompoundStatement(
                                                    new PrintStatement(new VariableExpression("v")),
                                                    new AssignmentStatement("v", new ArithmeticalExpression(
                                                            new VariableExpression("v"),
                                                            MINUS,
                                                            new ValueExpression(new IntValue(1))
                                                    ))
                                            ),
                                            new RelationalExpression(
                                                    new VariableExpression("v"),
                                                    new ValueExpression(new IntValue(0)),
                                                    GREATER
                                            )
                                    ),
                                    new PrintStatement(new VariableExpression("v"))
                            )
                    )
            );

            ex8.typeCheck(new MyMap<>());
            statements.add(ex8);
        } catch (ADTException | StatementException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
        }

        try {
            IStatement ex9 = new CompoundStatement(
                    new VarDeclStatement("v", new RefType(new IntType())),
                    new CompoundStatement(
                            new HeapAllocationStatement("v", new ValueExpression(new IntValue(20))),
                            new CompoundStatement(
                                    new VarDeclStatement("a", new RefType(new RefType(new IntType()))),
                                    new CompoundStatement(
                                            new HeapAllocationStatement("a", new VariableExpression("v")),
//                                        new HeapAllocationStatement("v", new ValueExpression(new IntValue(3)))
                                            new CompoundStatement(
                                                    new HeapAllocationStatement("v", new ValueExpression(new IntValue(3))),
                                                    new HeapAllocationStatement("a", new VariableExpression("v"))
                                            )
                                    )
                            )
                    ));

            ex9.typeCheck(new MyMap<>());
            statements.add(ex9);
        } catch (ADTException | StatementException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
        }

        try {
            IStatement ex10 = new CompoundStatement(
                    new VarDeclStatement("v", new IntType()),
                    new CompoundStatement(
                            new VarDeclStatement("a", new RefType(new IntType())),
                            new CompoundStatement(
                                    new AssignmentStatement("v", new ValueExpression(new IntValue(10))),
                                    new CompoundStatement(
                                            new HeapAllocationStatement("a", new ValueExpression(new IntValue(22))),
                                            new CompoundStatement(
                                                    new ForkStatement(
                                                            new CompoundStatement(
                                                                    new HeapWritingStatement("a", new ValueExpression(new IntValue(30))),
                                                                    new CompoundStatement(
                                                                            new AssignmentStatement("v", new ValueExpression(new IntValue(32))),
                                                                            new CompoundStatement(
                                                                                    new PrintStatement(new VariableExpression("v")),
                                                                                    new PrintStatement(new HeapReadingExpression(new VariableExpression("a")))
                                                                            )
                                                                    )
                                                            )
                                                    ),
                                                    new CompoundStatement(
                                                            new PrintStatement(new VariableExpression("v")),
                                                            new PrintStatement(new HeapReadingExpression(new VariableExpression("a")))
                                                    )
                                            )
                                    )
                            )
                    )
            );

            ex10.typeCheck(new MyMap<>());
            statements.add(ex10);
        } catch (ADTException | StatementException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
        }

        try {
            IStatement ex11 = new CompoundStatement(
                    new VarDeclStatement("v", new RefType(new IntType())),
                    new CompoundStatement(
                            new HeapAllocationStatement("v", new ValueExpression(new IntValue(20))),
                            new CompoundStatement(
                                    new ForkStatement(new CompoundStatement(
                                            new VarDeclStatement("a", new RefType(new RefType(new IntType()))),
                                            new CompoundStatement(
                                                    new HeapAllocationStatement("a", new VariableExpression("v")),
                                                    new HeapAllocationStatement("v", new ValueExpression(new IntValue(30)))
                                            ))),
                                    new CompoundStatement(
                                            new NopStatement(),
                                            new CompoundStatement(
                                                    new NopStatement(),
                                                    new HeapAllocationStatement("v", new ValueExpression(new IntValue(40)))
                                            )
                                    )
                            )
                    )
            );

            ex11.typeCheck(new MyMap<>());
            statements.add(ex11);
        } catch (ADTException | StatementException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
        }

        try {
            IStatement ex12 = new CompoundStatement(
                    // Declare variables: int v; int x; int y; v = 0;
                    new VarDeclStatement("v", new IntType()),
                    new CompoundStatement(
                            new VarDeclStatement("x", new IntType()),
                            new CompoundStatement(
                                    new VarDeclStatement("y", new IntType()),
                                    new CompoundStatement(
                                            new AssignmentStatement("v", new ValueExpression(new IntValue(0))),
                                            new CompoundStatement(
                                                    // Repeat-Until Block
                                                    new RepeatStatement(
                                                            new CompoundStatement(
                                                                    new ForkStatement(
                                                                            new CompoundStatement(
                                                                                    new PrintStatement(new VariableExpression("v")),
                                                                                    new AssignmentStatement(
                                                                                            "v",
                                                                                            new ArithmeticalExpression(
                                                                                                    new VariableExpression("v"),
                                                                                                    ArithmeticalOperator.MINUS,
                                                                                                    new ValueExpression(new IntValue(1))
                                                                                            )
                                                                                    )
                                                                            )
                                                                    ),
                                                                    new AssignmentStatement(
                                                                            "v",
                                                                            new ArithmeticalExpression(
                                                                                    new VariableExpression("v"),
                                                                                    ArithmeticalOperator.PLUS,
                                                                                    new ValueExpression(new IntValue(1))
                                                                            )
                                                                    )
                                                            ),
                                                            new RelationalExpression(
                                                                    new VariableExpression("v"),
                                                                    new ValueExpression(new IntValue(3)),
                                                                    RelationalOperator.EQUAL
                                                            )
                                                    ),
                                                    new CompoundStatement(
                                                            // Post Repeat Block
                                                            new AssignmentStatement("x", new ValueExpression(new IntValue(1))),
                                                            new CompoundStatement(
                                                                    new NopStatement(),
                                                                    new CompoundStatement(
                                                                            new AssignmentStatement("y", new ValueExpression(new IntValue(3))),
                                                                            new CompoundStatement(
                                                                                    new NopStatement(),
                                                                                    // Final Print Statement
                                                                                    new PrintStatement(
                                                                                            new ArithmeticalExpression(
                                                                                                    new VariableExpression("v"),
                                                                                                    ArithmeticalOperator.MULTIPLY,
                                                                                                    new ValueExpression(new IntValue(10))
                                                                                            )
                                                                                    )
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            )
                                    )
                            )
                    )
            );

            ex12.typeCheck(new MyMap<>());
            statements.add(ex12);
        } catch (ADTException | StatementException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
        }

        try {
            IStatement ex13 = new CompoundStatement(
                    new VarDeclStatement("v1", new RefType(new IntType())),
                    new CompoundStatement(
                            new VarDeclStatement("v2", new RefType(new IntType())),
                            new CompoundStatement(
                                    new VarDeclStatement("v3", new RefType(new IntType())),
                                    new CompoundStatement(
                                            new VarDeclStatement("cnt", new IntType()),
                                            new CompoundStatement(
                                                    new HeapAllocationStatement("v1", new ValueExpression(new IntValue(2))),
                                                    new CompoundStatement(
                                                            new HeapAllocationStatement("v2", new ValueExpression(new IntValue(3))),
                                                            new CompoundStatement(
                                                                    new HeapAllocationStatement("v3", new ValueExpression(new IntValue(4))),
                                                                    new CompoundStatement(
                                                                            new NewBarrierStatement("cnt", new HeapReadingExpression(new VariableExpression("v2"))),
                                                                            new CompoundStatement(
                                                                                    new ForkStatement(
                                                                                            new CompoundStatement(
                                                                                                    new AwaitStatement("cnt"),
                                                                                                    new CompoundStatement(
                                                                                                            new HeapWritingStatement("v1", new ArithmeticalExpression(
                                                                                                                    new HeapReadingExpression(new VariableExpression("v1")),
                                                                                                                    ArithmeticalOperator.MULTIPLY,
                                                                                                                    new ValueExpression(new IntValue(10))
                                                                                                            )),
                                                                                                            new PrintStatement(new HeapReadingExpression(new VariableExpression("v1")))
                                                                                                    )
                                                                                            )
                                                                                    ),
                                                                                    new CompoundStatement(
                                                                                            new ForkStatement(
                                                                                                    new CompoundStatement(
                                                                                                            new AwaitStatement("cnt"),
                                                                                                            new CompoundStatement(
                                                                                                                    new HeapWritingStatement("v2", new ArithmeticalExpression(
                                                                                                                            new HeapReadingExpression(new VariableExpression("v2")),
                                                                                                                            ArithmeticalOperator.MULTIPLY,
                                                                                                                            new ValueExpression(new IntValue(10))
                                                                                                                    )),
                                                                                                                    new CompoundStatement(
                                                                                                                            new HeapWritingStatement("v2", new ArithmeticalExpression(
                                                                                                                                    new HeapReadingExpression(new VariableExpression("v2")),
                                                                                                                                    ArithmeticalOperator.MULTIPLY,
                                                                                                                                    new ValueExpression(new IntValue(10))
                                                                                                                            )),
                                                                                                                            new PrintStatement(new HeapReadingExpression(new VariableExpression("v2")))
                                                                                                                    )
                                                                                                            )
                                                                                                    )
                                                                                            ),
                                                                                            new CompoundStatement(
                                                                                                    new AwaitStatement("cnt"),
                                                                                                    new PrintStatement(new HeapReadingExpression(new VariableExpression("v3")))
                                                                                            )
                                                                                    )
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            )
                                    )
                            )
                    )
            );

            ex13.typeCheck(new MyMap<>());
            statements.add(ex13);
        } catch (ADTException | StatementException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
        }

        return statements;
    }

    @FXML
    public void onRunBtnClick() {
        IStatement selectedStatement = prgStateList.getSelectionModel().getSelectedItem();
        if (selectedStatement == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a statement to run");
            alert.show();
        }
        else {
            try {
                selectedStatement.typeCheck(new MyMap<>());
                PrgState prg = new PrgState(selectedStatement);
                int index = prgStateList.getSelectionModel().getSelectedIndex();
                IRepository repo = new Repository("log" + (index + 1) +".txt");
                repo.add(prg);
                ExecutorService executor = Executors.newFixedThreadPool(10);
                Controller ctr = new Controller(repo, executor, true);

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("execute-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 820, 540);
                Stage stage = new Stage();
                stage.setTitle("Executor");
                stage.setScene(scene);
                ExecuteViewController ctrView = fxmlLoader.getController();
                ctrView.setController(ctr);
                stage.show();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Type check error");
                alert.show();
            }
        }
    }
}
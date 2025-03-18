package view;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.ADT.MyIMap;
import model.ADT.MyMap;
import model.expressions.*;
import model.statements.*;
import model.values.*;
import model.types.*;
import model.state.PrgState;
import repository.*;
import controller.Controller;
import view.commands.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static model.expressions.ArithmeticalOperator.*;
import static model.expressions.LogicalOperator.*;
import static model.expressions.RelationalOperator.*;

public class Interpreter {
    public static void main(String[] args) {

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "Exit"));
        try {
            // Example 1: string varf; varf = "test.in"; openRFile(varf); int varc; readFile(varf, varc);
            // print(varc); readFile(varf, varc); print(varc); closeRFile(varf);
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
            PrgState prg1 = new PrgState(ex1);
            IRepository repo1 = new Repository("log1.txt");
            repo1.add(prg1);
            ExecutorService executor1 = Executors.newFixedThreadPool(2);
            Controller ctr1 = new Controller(repo1, executor1, true);
            menu.addCommand(new RunExample("1", "string varf; varf = \"test.in\"; openRFile(varf); int varc; readFile(varf, varc); " +
                    "print(varc); readFile(varf, varc); print(varc); closeRFile(varf);", ctr1));
        } catch (StatementException | ADTException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
            System.exit(1);
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
            PrgState prg2 = new PrgState(ex2);
            IRepository repo2 = new Repository("log2.txt");
            repo2.add(prg2);
            ExecutorService executor2 = Executors.newFixedThreadPool(2);
            Controller ctr2 = new Controller(repo2, executor2, true);
            menu.addCommand(new RunExample("2", "int a; a = 5; int b; b = a + 10; print(b);", ctr2));
        } catch (StatementException | ADTException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
            System.exit(1);
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
            PrgState prg3 = new PrgState(ex3);
            IRepository repo3 = new Repository("log3.txt");
            repo3.add(prg3);
            ExecutorService executor3 = Executors.newFixedThreadPool(2);
            Controller ctr3 = new Controller(repo3, executor3, true);
            menu.addCommand(new RunExample("3", "int x; x = 20; int y; if (x > 10) then y = 1 else y = 0; print(y);", ctr3));

        } catch (StatementException | ADTException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
            System.exit(1);
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
            PrgState prg4 = new PrgState(ex4);
            IRepository repo4 = new Repository("log4.txt");
            repo4.add(prg4);
            ExecutorService executor4 = Executors.newFixedThreadPool(2);
            Controller ctr4 = new Controller(repo4, executor4, true);
            menu.addCommand(new RunExample("4", "int v; new(v, 20); print(rH(v));", ctr4));
        } catch (StatementException | ADTException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
            System.exit(1);
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
            PrgState prg5 = new PrgState(ex5);
            IRepository repo5 = new Repository("log5.txt");
            repo5.add(prg5);
            ExecutorService executor5 = Executors.newFixedThreadPool(2);
            Controller ctr5 = new Controller(repo5, executor5, true);
            menu.addCommand(new RunExample("5", "int a; new(a, 20); int b; new(b, a); print(rH(rH(b)));", ctr5));
        } catch (StatementException | ADTException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
            System.exit(1);
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
            PrgState prg6 = new PrgState(ex6);
            IRepository repo6 = new Repository("log6.txt");
            repo6.add(prg6);
            ExecutorService executor6 = Executors.newFixedThreadPool(2);
            Controller ctr6 = new Controller(repo6, executor6, true);
            menu.addCommand(new RunExample("6", "int v; new(v, 20); print(rH(v)); wH(v, 30); print(rH(v) + 5);", ctr6));
        } catch (StatementException | ADTException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
            System.exit(1);
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
            PrgState prg7 = new PrgState(ex7);
            IRepository repo7 = new Repository("log7.txt");
            repo7.add(prg7);
            ExecutorService executor7 = Executors.newFixedThreadPool(2);
            Controller ctr7 = new Controller(repo7, executor7, true);
            menu.addCommand(new RunExample("7", "int v1; new(v1, 20); int v2; new(v2, 30); int v3; new(v3, v2); wH(v1, 50); print(rH(v1));", ctr7));
        } catch (StatementException | ADTException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
            System.exit(1);
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
            PrgState prg8 = new PrgState(ex8);
            IRepository repo8 = new Repository("log8.txt");
            repo8.add(prg8);
            ExecutorService executor8 = Executors.newFixedThreadPool(2);
            Controller ctr8 = new Controller(repo8, executor8, true);
            menu.addCommand(new RunExample("8", "int v; v = 5; while (v > 0) print(v); v = v - 1; print(v);", ctr8));
        } catch (StatementException | ADTException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
            System.exit(1);
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
            PrgState prg9 = new PrgState(ex9);
            IRepository repo9 = new Repository("log9.txt");
            repo9.add(prg9);
            ExecutorService executor9 = Executors.newFixedThreadPool(2);
            Controller ctr9 = new Controller(repo9, executor9, true);
            menu.addCommand(new RunExample("9", "ref int v; new(v, 20); ref ref int a; new(a, v); new(v, 3); new(a, v)", ctr9));
        } catch (StatementException | ADTException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
            System.exit(1);
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
            PrgState prg10 = new PrgState(ex10);
            IRepository repo10 = new Repository("log10.txt");
            repo10.add(prg10);
            ExecutorService executor10 = Executors.newFixedThreadPool(2);
            Controller ctr10 = new Controller(repo10, executor10, true);
            menu.addCommand(new RunExample("10", "int v; ref int a; v = 10; new(a, 22); fork(wH(a, 30); v = 32; print(v); print(rH(a));); print(v); print(rH(a));", ctr10));
        } catch (StatementException | ADTException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
            System.exit(1);
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
            PrgState prg11 = new PrgState(ex11);
            IRepository repo11 = new Repository("log11.txt");
            repo11.add(prg11);
            ExecutorService executor11 = Executors.newFixedThreadPool(2);
            Controller ctr11 = new Controller(repo11, executor11, true);
            menu.addCommand(new RunExample("11", "ref int v; new(v, 20); fork(ref int a; new(a, v); new(v, 30);); new(v, 40);", ctr11));
        } catch (StatementException | ADTException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
            System.exit(1);
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
            PrgState prg12 = new PrgState(ex12);
            IRepository repo12 = new Repository("log12.txt");
            repo12.add(prg12);
            ExecutorService executor12 = Executors.newFixedThreadPool(10);
            Controller ctr12 = new Controller(repo12, executor12, true);
            menu.addCommand(new RunExample("12", "int v; int x; int y; v = 0; repeat { fork(print(v); v = v - 1); v = v + 1; } until (v == 3); x = 1; y = 3; print(v * 10);", ctr12));
        } catch (StatementException | ADTException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
            System.exit(1);
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
            PrgState prg13 = new PrgState(ex13);
            IRepository repo13 = new Repository("log13.txt");
            repo13.add(prg13);
            ExecutorService executor13 = Executors.newFixedThreadPool(10);
            Controller ctr13 = new Controller(repo13, executor13, true);
            menu.addCommand(new RunExample("13", "ref int v1; ref int v2; ref int v3; int cnt; new(v1, 2); new(v2, 3); new(v3, 4); newBarrier(cnt, rH(v2)); fork(await(cnt); wH(v1, rH(v1) * 10); print(rH(v1));); fork(await(cnt); wH(v2, rH(v2) * 10); print(rH(v2));); fork(await(cnt); wH(v3, rH(v3) * 10); print(rH(v3));); await(cnt); print(100);", ctr13));
        } catch (StatementException | ADTException | ExpressionException e) {
            System.out.println("Type checking error: " + e.getMessage());
            System.exit(1);
        }

        try {
            menu.show();
        } catch (ADTException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
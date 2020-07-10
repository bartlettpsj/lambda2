package com.paul;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@FunctionalInterface
interface BinaryCalculator {
    double calculate(double value1, double value2);
}

@FunctionalInterface
interface UnaryCalculator {
    double calculate(double value);
}

@FunctionalInterface
interface Runnable3 {
    public abstract String run(String a, Boolean b, Integer c);
}

@FunctionalInterface
interface Invoker {
    void invoke(Object ... args);
}

class Classy {
    private Integer number = 0;
    Classy(int i) { number = i; }
    public int getNumber() { return number; }
    public void setNumber(Integer number) { this.number = number; }
}

public class Main {

    int secret_number = 786;

    // Takes a function interface and calls it
    static void funky(Runnable3 rrr) {
        String s = rrr.run("a", true, 1);
        System.out.println(s);
    }

    // Silly code using pathMatcher but demonstrates nested lambda functions
    static boolean checkList(PathMatcher[] pathMatchers, List list) {
        return list.stream().anyMatch(item -> {
            Path path = new File(item.toString()).toPath();
            return Arrays.stream(pathMatchers).anyMatch(matcher -> matcher.matches(path));
        });
    }

    // Using this in a non-static class within an anonymous function
    public void doWork() {
        System.out.printf("This is doWork\n");
        new Thread(() -> System.out.printf("this = %d\n", this.secret_number)).start();
    }

    static void doThing(Predicate<Integer> tester, int number) {
        if (tester.test(number)) {
            System.out.printf("%d passes the test\n", number);
        } else {
            System.out.printf("%d fails the test\n", number);
        }
    }

    static void modifyMe(Consumer<Classy> consumer, Classy number) {
        System.out.printf("Modifying the damn thing from %d ", number.getNumber());
        consumer.accept(number);
        System.out.printf("to %d \n", number.getNumber());
    }

    public static void printInt(Supplier<Integer> supplier)
    {
        System.out.println(supplier.get());
    }

    public static void funkStr(Function<String, String> funk, String arg) {
        System.out.println(funk.apply(arg));
    }

    public static void invokeInvoker(Invoker invoker, Object ... args) {
        invoker.invoke(args);
    }

    public static boolean runThem(Runnable runnables[]) {
        Arrays.stream(runnables).forEach( runnable -> runnable.run());
        return true;
    }

    public static boolean forkThem(Runnable runnables[]) {
        Arrays.stream(runnables).forEach( runnable -> new Thread(runnable).start());
        return true;
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            // Do nothing
        }
    }

    public static void main(String[] args) {
        Runnable f = () -> System.out.printf("Hello %s\n", "s");
        f.run();

        Runnable2 r = (a, b) -> System.out.printf("Hello %f - %d\n", a, b);
        r.run(1.1, 2);

        Thread hello = new Thread(() -> System.out.printf("Hello %s\n", "f"));
        hello.start();

        // Pre Lambda version - implement runnable interface and pass to thread object
        Runnable x = new Runnable() {
            @Override
            public void run() {
                System.out.printf("Hello Runnable\n");
            }
        };

        new Thread(x).start();

        // Parameterized lambdas
        Runnable2 j = (double d, int b) -> System.out.printf("hello\n");

        UnaryCalculator calculatorOne = a -> a + a;
        System.out.printf("Calculation %f\n", calculatorOne.calculate(1.1));

        // Pass lambda as argument
        Runnable3 r3 = (a, b, c) -> String.format("%s %b %d", a, b, c);
        funky(r3);


        File[] files = new File(".").listFiles();
        Arrays.stream(files).forEach((file) -> {
            System.out.println(file);
        });

        // Short way!
        Arrays.stream(files).forEach(System.out::println);

        PathMatcher matcher[] = {
                (path) -> {
                    System.out.println("check txt");
                    return path.toString().endsWith("jsp");
                },
                (path) -> path.toString().endsWith("svg")
        };

        boolean result = checkList(matcher, Arrays.asList("paul.txt", "fred.com", "bert.png"));
        System.out.printf("Checklist %b\n", result);

        new Main().doWork();

        doThing( (value) -> value > 0, 1);
        doThing( (value) -> value > 0, -1);
        doThing( (value) -> value % 2 == 0, 2);

        Classy modify = new Classy(123);
        modifyMe( (num) -> {
            System.out.printf("Changing %d to ", num.getNumber());
            num.setNumber(num.getNumber()+1);
            System.out.printf("%d\n", num.getNumber()); },
                modify);
        System.out.printf("Number changed to: %d\n", modify.getNumber());

        Consumer<String> consumer1 = (s) -> System.out.println(s);
        consumer1.accept("hello");

        Consumer consumer2 = System.out::println; // The method reference is shorter.
        consumer2.accept("Hello2");

        Consumer<String> consumer3 = System.out::printf;
        consumer3.accept("hello3\n");

        int[] array = { 10, 2, 19, 5, 17 };
        Consumer<int[]> consumer = Arrays::sort;
        consumer.accept(array);
        Arrays.stream(array).forEach(System.out::println);

        printInt( ()-> 123);

        String s = "Paul was here and there";
        printInt(s::length);

        funkStr(str -> str.toLowerCase(), "UPPER");

        // Do some invoking

        invokeInvoker( (a) -> {
            System.out.printf("%s\n", a);
        }, "1", "2", "C");


        // Work out how to run two different things using threads

        Runnable runnables[] = { () -> {
            System.out.println("Hello");
        }, () -> {
            System.out.println("Goodbye");
        }, () -> {
            sleep(2000);
            System.out.println("Test");
        }};

        runThem(runnables);
        forkThem(runnables);

        System.out.println("---done---");
    }

}

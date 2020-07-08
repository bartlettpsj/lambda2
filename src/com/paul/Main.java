package com.paul;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;

@FunctionalInterface
interface BinaryCalculator
{
    double calculate(double value1, double value2);
}

@FunctionalInterface
interface UnaryCalculator
{
    double calculate(double value);
}

@FunctionalInterface
interface Runnable3
{
    public abstract String run( String a, Boolean b, Integer c);
}

public class Main {

    // Takes a function interface and calls it
    static void funky(Runnable3 rrr) {
        String s = rrr.run("a", true, 1);
        System.out.println(s);
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

        UnaryCalculator calculatorOne = a -> a+a;
        System.out.printf("Calculation %f\n", calculatorOne.calculate(1.1));

        // Pass lambda as argument
        Runnable3 r3 = (a,b,c) -> String.format("%s %b %d", a, b, c);
        funky(r3);


        File[] files = new File(".").listFiles();
        Arrays.stream(files).forEach( (file) -> {
            System.out.println(file);
        });

        Arrays.stream(files).forEach(System.out::println);

        System.out.println("done");
    }

}

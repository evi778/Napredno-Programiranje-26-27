package zad10;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.avarage());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.avarage());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.avarage());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
// vasiot kod ovde

class Triple<T extends Number & Comparable<T>> {
    List<T> triple;

    public Triple(T a, T b, T c) {
        triple = List.of(a, b, c);
    }

    public double max() {
        return triple.stream().mapToDouble(Number::doubleValue).max().getAsDouble();
    }

    public double avarage() {
        return triple.stream().mapToDouble(Number::doubleValue).sum() / 3.00;
    }

    void sort() {
        triple = triple.stream().sorted(Comparator.comparing(Number::doubleValue)).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public String toString() {
        return String.format("%.2f %.2f %.2f", triple.get(0).doubleValue(), triple.get(1).doubleValue(), triple.get(2).doubleValue());
    }
}



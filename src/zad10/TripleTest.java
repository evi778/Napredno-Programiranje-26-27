package zad10;

import java.util.Scanner;

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
class Triple<T extends Number> {
    T a;
    T b;
    T c;

    public Triple(T a, T b, T c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double max() {
        double A = a.doubleValue();
        double B = b.doubleValue();
        double C = c.doubleValue();

        if (A >= B && A >= C) {
            return A;
        }
        if (B >= C) {
            return B;
        }
        return C;
    }

    public double avarage() {
        double A = a.doubleValue();
        double B = b.doubleValue();
        double C = c.doubleValue();

        return (A + B + C) / 3.0;
    }


    public void sort() {
        double A = a.doubleValue();
        double B = b.doubleValue();
        double C = c.doubleValue();

        if (C == this.max()) {
            if (A > B) {
                T temp = b;
                b = a;
                a = temp;
            }
        } else if (B == this.max()) {
            T temp = b;
            b = c;
            c = temp;

            if (C < A) {
                T tmp = a;
                a = b;
                b = tmp;
            }

        } else {
            T temp = a;
            a = c;
            c = temp;

            if (C > B) {
                T tmp = a;
                a = b;
                b = tmp;
            }
        }
    }

    @Override
    public String toString() {
        return (String.format("%.2f %.2f %.2f", a.doubleValue(), b.doubleValue(), c.doubleValue()));
    }
}




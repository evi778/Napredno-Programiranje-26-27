package zad5;

import java.util.Scanner;


public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for (int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for (int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}

class MinMax<T extends Comparable<T>> {
    T min = null;
    T max = null;
    int diff, dup_max, dup_min;

    MinMax() {
        diff = 0;
        dup_max = 0;
        dup_min = 0;
    }

    void update(T element) {
        if (min == null) {
            min = element;
            max = element;
            return;
        }

        if (element.compareTo(min) == 0) {
            if (min.compareTo(max) == 0) dup_max++;
            dup_min++;
            return;
        }

        if (element.compareTo(max) == 0) {
            dup_max++;
            return;
        }

        if (element.compareTo(min) < 0) {

            if (min.compareTo(max) != 0) {
                diff += dup_min + 1;
            }

            min = element;
            dup_min = 0;
            return;
        }


        if (element.compareTo(max) > 0) {

            if (min.compareTo(max) != 0) {
                diff += dup_max + 1;
            }

            max = element;
            dup_max = 0;
            return;
        }

        diff++;

    }

    T max() {
        return max;
    }

    T min() {
        return min;
    }

    @Override
    public String toString() {
        return min + " " + max + " " + diff + "\n";
    }
}
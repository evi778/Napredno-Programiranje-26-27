package zad5;

import java.util.Scanner;

class MinMax<T extends Comparable<T>> {

    T min, max;
    int counter, dup_min, dup_max;

    MinMax() {
        min = null;
        max = null;
        counter = 0;
        dup_min = 0;
        dup_max = 0;
    }

    void update(T element) {
        if (min == null || max == null) {
            min = element;
            max = element;
            return;
        }

        if (element.compareTo(min) == 0) {
            dup_min++;
            return;
        }
        if (element.compareTo(max) == 0) {
            dup_max++;
            return;
        }


        if (max.compareTo(min) == 0) {

            if (element.compareTo(min) == 0) {
                dup_min++;
                dup_max++;
            }

            if (min.compareTo(element) > 0) {
                min = element;
                dup_min = 0;
            } else {
                max = element;
                dup_max = 0;
            }
        } else if (min.compareTo(element) > 0) {
            min = element;
            counter += dup_min + 1;
            dup_min = 0;
        } else if (max.compareTo(element) < 0) {
            max = element;
            counter += dup_max + 1;
            dup_max = 0;
        } else {
            counter++;
        }


    }

    T min() {
        return min;
    }

    T max() {
        return max;
    }

    @Override
    public String toString() {
        return min +
                " " + max +
                " " + counter + "\n";
    }
}

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
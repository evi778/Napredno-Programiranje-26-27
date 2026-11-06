package zad6;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Collectors;


enum Color {
    RED, GREEN, BLUE
}

interface Scalable {
    void scale(float scaleFactor);
}

interface Stackable {
    float weight();
}

class Shape implements Scalable, Stackable {
    String id;
    Color color;

    public Shape(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void scale(float scaleFactor) {

    }

    @Override
    public float weight() {
        return 0;
    }

}

class Circle extends Shape {
    float radius;

    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    public void scale(float scaleFactor) {
        this.radius *= scaleFactor;
    }

    @Override
    public float weight() {
        return radius * radius * Float.parseFloat(String.valueOf(Math.PI));
    }

    @Override
    public String toString() {
        return String.format("C: %-5s%-10s%10.2f\n", id, color, weight());
    }
}

class Rectangle extends Shape {
    float width;
    float height;

    public Rectangle(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public void scale(float scaleFactor) {
        this.width *= scaleFactor;
        this.height *= scaleFactor;
    }

    @Override
    public float weight() {
        return width * height;
    }

    @Override
    public String toString() {
        return String.format("R: %-5s%-10s%10.2f\n", id, color, weight());
    }

}

class Canvas implements Comparator<Shape> {
    ArrayList<Shape> shapes;

    public Canvas() {
        shapes = new ArrayList<Shape>();
    }


    int findIndex(Shape c) {

        for (int i = shapes.size() - 1; i >= 0; i--) {
            if (compare(c, shapes.get(i)) <= 0) {
                return i + 1;
            }
        }

        return 0;
    }


    void add(String id, Color color, float radius) {
        Circle c = new Circle(id, color, radius);

        int index = findIndex(c);

        shapes.add(index, c);

    }


    void add(String id, Color color, float width, float height) {
        Rectangle r = new Rectangle(id, color, width, height);

        int index = findIndex(r);

        shapes.add(index, r);

    }

    void scale(String id, float scaleFactor) {
        for (Shape s : shapes) {
            if (s.id.equals(id)) {
                shapes.remove(s);
                s.scale(scaleFactor);
                int index = findIndex(s);
                shapes.add(index, s);
                return;
            }
        }

    }

    @Override
    public int compare(Shape o1, Shape o2) {
        if (o1.weight() < o2.weight()) {
            return -1;
        } else if (o1.weight() > o2.weight()) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return shapes.stream().map(s -> s.toString()).collect(Collectors.joining(""));
    }
}

public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}

package zad6;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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

enum Color {
    RED, GREEN, BLUE
}

interface Scalable {
    void scale(float scaleFactor);
}

interface Stackable {
    float weight();
}

abstract class Shape implements Scalable, Stackable {
    String id;
    Color color;

    public Shape(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    public String getType() {
        return "";
    }

    public float weight() {
        return 0;
    }

    public void scale(float scaleFactor) {
        return;
    }
}

class Circle extends Shape {
    float radius;

    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    public String getType() {
        return "Circle";
    }

    @Override
    public float weight() {
        return (float) (radius * radius * Math.PI);
    }

    @Override
    public void scale(float scaleFactor) {
        radius = radius * scaleFactor;
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
    public String getType() {
        return "Rectangle";
    }

    @Override
    public float weight() {
        return height * width;
    }

    @Override
    public void scale(float scaleFactor) {
        height = height * scaleFactor;
        width = width * scaleFactor;
    }

    @Override
    public String toString() {
        return String.format("R: %-5s%-10s%10.2f\n", id, color, weight());
    }
}

class Canvas {
    List<Shape> shapes;

    public Canvas() {
        this.shapes = new ArrayList<>();
    }

    private int find_index(float w) {
        int i = 0;
        while (i < shapes.size() && shapes.get(i).weight() >= w) {
            i++;
        }
        return i;
    }

    public void add(String id, Color color, float radius) {
        Circle shape = new Circle(id, color, radius);
        int index = find_index(shape.weight());
        shapes.add(index, shape);
    }

    public void add(String id, Color color, float width, float height) {
        Rectangle shape = new Rectangle(id, color, width, height);
        int index = find_index(shape.weight());
        shapes.add(index, shape);
    }

    public void scale(String id, float scaleFactor) {
        Shape shape = shapes.stream().filter(s -> s.id.equals(id)).findFirst().orElse(null);

        if (shape != null) {
            shapes.remove(shape);
            shape.scale(scaleFactor);
            int index = find_index(shape.weight());
            shapes.add(index, shape);
        }
    }

    @Override
    public String toString() {
        return shapes.stream().map(Object::toString).collect(Collectors.joining(""));
    }
}
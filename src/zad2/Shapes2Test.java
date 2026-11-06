package zad2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.io.*;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

class IrregularCanvasException extends Exception {

    public IrregularCanvasException(String canvas_id, double max_area) {
        super("Canvas " + canvas_id + " has a shape with area larger than " + String.format("%.2f", max_area));
    }
}

abstract class Shape {
    double size;

    public Shape(double size) {
        this.size = size;
    }

    public double getSize() {
        return size;
    }

    public double getArea() {
        return 0;
    }

}

class Circle extends Shape {

    public Circle(double radius) {
        super(radius);
    }

    @Override
    public double getArea() {
        return size * size * Math.PI;
    }
}

class Square extends Shape {

    public Square(double side) {
        super(side);
    }

    @Override
    public double getArea() {
        return size * size;
    }
}


class ShapeCanvas {
    private String id;
    private ArrayList<Shape> shapes;

    double minArea = 1000000000;
    double maxArea = 0;
    double averageArea = 0;
    int circle_count = 0;
    int square_count = 0;


    ShapeCanvas(String id, ArrayList<Shape> shapes) {
        this.id = id;
        this.shapes = shapes;

        for (Shape shape : shapes) {
            double area = shape.getArea();
            if (area > maxArea) {
                maxArea = area;
            }
            if (area < minArea) {
                minArea = area;
            }
            averageArea += area;

            if (shape instanceof Square) {
                square_count += 1;
            } else {
                circle_count += 1;
            }
        }

        averageArea /= this.getSize();
    }

    public String getId() {
        return id;
    }

    public int getSize() {
        return shapes.size();
    }

    public double getMinArea() {
        return minArea;
    }

    public double getMaxArea() {
        return maxArea;
    }

    public double getAverageArea() {
        return averageArea;
    }

    public double getTotalArea() {
        return shapes.stream()
                .mapToDouble(Shape::getArea)
                .sum();
    }

    public int getTotalCircles() {
        return circle_count;
    }

    public int getTotalSquares() {
        return square_count;
    }
}

class ShapesApplication {
    private final ArrayList<ShapeCanvas> canvas;
    private double maxArea;

    public ShapesApplication() {
        this.canvas = new ArrayList<>();
    }

    public ShapesApplication(double maxArea) {
        this.canvas = new ArrayList<>();
        this.maxArea = maxArea;
    }


    public void readCanvases(InputStream in) throws IOException, IrregularCanvasException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));


        reader.lines().forEach(line -> {

            String[] parts = line.split(" ");
            String canvasId = parts[0];

            ArrayList<Shape> shapes = new ArrayList<>();

            try {

                for (int i = 1; i < parts.length; i += 2) {

                    String type = parts[i];
                    double size = Double.parseDouble(parts[i + 1]);

                    Shape shape;

                    if (type.equals("S")) {
                        shape = new Square(size);
                    } else {
                        shape = new Circle(size);
                    }

                    if (shape.getArea() > maxArea) {
                        throw new IrregularCanvasException(canvasId, maxArea);
                    }

                    shapes.add(shape);
                }

                ShapeCanvas shapeCanvas = new ShapeCanvas(canvasId, shapes);

                canvas.add(shapeCanvas);


            } catch (IrregularCanvasException e) {
                System.out.println(e.getMessage());
            }
        });

    }

    public void printCanvases(PrintStream out) {

        PrintWriter pw = new PrintWriter(out);

        canvas.stream()
                .sorted(Comparator.comparingDouble(
                        ShapeCanvas::getTotalArea
                ).reversed())
                .forEach(canvas -> {

                    pw.printf(
                            "%s %d %d %d %.2f %.2f %.2f%n",
                            canvas.getId(),
                            canvas.getSize(),
                            canvas.getTotalCircles(),
                            canvas.getTotalSquares(),
                            canvas.getMinArea(),
                            canvas.getMaxArea(),
                            canvas.getAverageArea()
                    );
                });

        pw.flush();
    }
}

public class Shapes2Test {

    public static void main(String[] args) throws IOException, IrregularCanvasException {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);

    }

}

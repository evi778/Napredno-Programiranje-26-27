package zad2;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Shapes2Test {

    public static void main(String[] args) throws IrregularCanvasException {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}

class IrregularCanvasException extends Exception {
    public IrregularCanvasException(String id, double max_area) {
        super(String.format("Canvas %s has a shape with area larger than %.2f", id, max_area));
    }
}

abstract class Shape {
    double size;

    public Shape(double size) {
        this.size = size;
    }

    double getArea() {
        return 0;
    }
}

class Square extends Shape {

    public Square(double size) {
        super(size);
    }

    @Override
    double getArea() {
        return size * size;
    }
}

class Circle extends Shape {

    public Circle(double size) {
        super(size);
    }

    @Override
    double getArea() {
        return  size * Math.PI * size;
    }
}

class Canvas {
    String id;
    List<Shape> shapes;
    int c_count, s_count;

    public Canvas(String line, double maxAreaPermited) throws IrregularCanvasException {
        String[] parts = line.split("\\s+");

        id = parts[0];
        shapes = new ArrayList<>();
        c_count = 0;
        s_count = 0;

        for (int i = 1; i < parts.length; i += 2) {
            if (parts[i].equals("C")) {
                Circle c = new Circle(Double.parseDouble(parts[i + 1]));
                if (c.getArea() > maxAreaPermited ){
                    throw new IrregularCanvasException(id,maxAreaPermited);
                }
                shapes.add(c);
                c_count++;
            } else {
                Square s = new Square(Double.parseDouble(parts[i + 1]));
                if (s.getArea() > maxAreaPermited ){
                    throw new IrregularCanvasException(id,maxAreaPermited);
                }
                shapes.add(s);
                s_count++;
            }
        }

    }

    public int getTotalCount() {
        return shapes.size();
    }


    public double getAreaSum() {
        return shapes.stream().mapToDouble(Shape::getArea).sum();
    }

    public double getMaxArea() {
        return shapes.stream().mapToDouble(Shape::getArea).max().orElse(0);
    }

    public double getMinArea() {
        return shapes.stream().mapToDouble(Shape::getArea).min().orElse(0);
    }

    @Override
    public String toString() {
        return String.format("%s %d %d %d %.2f %.2f %.2f",
                id, getTotalCount(), c_count, s_count, getMinArea(), getMaxArea(), (getAreaSum() / getTotalCount()));

    }
}


class ShapesApplication {
    List<Canvas> canvases;
    double maxArea;

    public ShapesApplication(double maxArea) {
        canvases = new ArrayList<>();
        this.maxArea = maxArea;
    }

    int readCanvases(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        br.lines().forEach(line -> {
            try{
                canvases.add(new Canvas(line,maxArea));
            }catch (IrregularCanvasException e){
                System.out.println(e.getMessage());
            }
        });

        return canvases.stream().mapToInt(Canvas::getTotalCount).sum();
    }


    public void printCanvases(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);

        canvases.stream()
                .sorted(Comparator.comparing(Canvas::getAreaSum).reversed())
                .forEach(pw::println);

        pw.flush();
    }
}
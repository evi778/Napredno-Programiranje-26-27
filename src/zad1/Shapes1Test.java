package zad1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}

class Shape{
    int size;

    public Shape(int size) {
        this.size = size;
    }

    int getL(){
        return 4*size;
    }
}

class Canvas{
    String id;
    List<Shape> shapes;

    public Canvas(String line) {
        String[] parts = line.split("\\s+");

        id = parts[0];
        shapes = new ArrayList<>();

        for(int i=1;i<parts.length;i++){
            shapes.add(new Shape(Integer.parseInt(parts[i])));
        }
    }

    public int getTotalCount(){
        return shapes.size();
    }

    public int getLSum(){
        return shapes.stream().mapToInt(Shape::getL).sum();
    }

    @Override
    public String toString() {
        return  id + " " + getTotalCount() + " " +getLSum();
    }
}


class ShapesApplication{
    List<Canvas> canvases;

    public ShapesApplication(){
        canvases = new ArrayList<>();
    }

    int readCanvases (InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        br.lines().forEach(line -> canvases.add(new Canvas(line)));

        return canvases.stream().mapToInt(Canvas::getTotalCount).sum();
    }

    void printLargestCanvasTo (OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);
        int maxL = canvases.stream().mapToInt(Canvas::getLSum).max().orElse(0);
        Canvas canvas = canvases.stream().filter(c -> c.getLSum()==maxL).findFirst().orElse(null);

        pw.println(canvas);
        pw.flush();
    }
}
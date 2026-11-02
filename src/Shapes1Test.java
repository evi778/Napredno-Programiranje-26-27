import java.io.BufferedReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Canvas2{
    private String  id;
    private ArrayList<Integer> squares;


    Canvas2(String id, ArrayList<Integer> squares){
        this.id = id;
        this.squares = squares;
    }

    public String getId() {
        return id;
    }

    int getSize(){
        return squares.size();
    }

    int getPerimetar(){
        int L=0;

        for(int i=0;i<squares.size();i++){
            L+=squares.get(i);
        }
        return L*4;
    }

}

class ShapesApplication2{
    private ArrayList<Canvas2> canvas;

    public ShapesApplication2() {
        this.canvas = new ArrayList<>();
    }


    public int readCanvases(InputStream in) throws IOException {

        int count = 0;
        String line;

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        while(( line = reader.readLine() ) != null){

            String[] parts = line.split(" ");

            String id =  parts[0];
            ArrayList<Integer> squares = new ArrayList<>();

            for(int i=1;i<parts.length;i++){
                squares.add(Integer.parseInt(parts[i]));
            }

            Canvas2 c = new Canvas2(id,squares);
            canvas.add(c);

            count += c.getSize();
        }

        return count;

    }

    public void printLargestCanvasTo(PrintStream out) {

        String id = canvas.get(0).getId();
        int size = canvas.get(0).getSize();
        int L = canvas.get(0).getPerimetar();

        for( int i = 1; i<canvas.size();i++){
            if ( L < canvas.get(i).getPerimetar()){
                id = canvas.get(i).getId();
                size = canvas.get(i).getSize();
                L = canvas.get(i).getPerimetar();
            }
        }

        System.out.print(id + " " + size + " " + L + "\n");
    }
}

public class Shapes1Test {

    public static void main(String[] args) throws IOException {
        ShapesApplication2 shapesApplication = new ShapesApplication2();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}
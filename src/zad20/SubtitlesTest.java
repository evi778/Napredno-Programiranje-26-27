package zad20;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class SubtitlesTest {
    public static void main(String[] args) throws IOException {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}

// Вашиот код овде

class Item {
    int id;
    int start;
    int end;
    String text;

    public Item(int id, int start, int end, String text) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.text = text;
    }

    public String msToTime(int ms) {
        int hour = ms / 3600000;
        ms -= hour * 3600000;

        int min = ms / 60000;
        ms -= min * 60000;


        int sec = ms / 1000;
        ms -= sec * 1000;

        return String.format("%02d:%02d:%02d,%03d", hour, min, sec,ms);
    }

    void shift(int ms) {
        start += ms;
        end += ms;
    }

    @Override
    public String toString() {
        return id + "\n" +
                msToTime(start) + " --> " + msToTime(end) + "\n" +
                text;

    }
}


class Subtitles {
    ArrayList<Item> items;

    public Subtitles() {
        items = new ArrayList<Item>();
    }

    public int timeToMs(String time) {
        String[] partsOfTime = time.split(":");
        int hour, min, sec, ms;

        hour = Integer.parseInt(partsOfTime[0]);
        min = Integer.parseInt(partsOfTime[1]);
        sec = Integer.parseInt(partsOfTime[2].split(",")[0]);
        ms = Integer.parseInt(partsOfTime[2].split(",")[1]);

        return hour * 3600000 + min * 60000 + sec * 1000 + ms;
    }


    int loadSubtitles(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        int elements_read = 0;
        String line;

        while ((line = br.readLine()) != null) {
            if (line.isBlank()) {
                continue;
            }

            int id = Integer.parseInt(line);

            String[] times = br.readLine().split(" --> ");
            int startTime = timeToMs(times[0]);
            int endTime = timeToMs(times[1]);

            String text_line;
            String text="";
            while((text_line = br.readLine())!=null && !text_line.isBlank()){
                text += text_line + "\n";
            }


            elements_read++;
            items.add(new Item(id, startTime, endTime, text));

        }

        return elements_read;
    }

    public void print() {
        items.forEach(item -> System.out.println(item.toString()));
    }

    void shift(int ms) {
        items.forEach(item -> item.shift(ms));
    }
}
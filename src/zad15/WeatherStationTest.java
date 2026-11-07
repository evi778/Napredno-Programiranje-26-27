package zad15;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }

}

// vashiot kod ovde

class WeatherReport {
    float temperature;
    float wind;
    float humidity;
    float visibility;
    Date date;

    public WeatherReport(float temperature, float wind, float humidity, float visibility, Date date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    @Override
    public String toString() {
    //  Tue Dec 17 23:30:15 GMT 2013
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        return temperature + " " +
                wind + " km/h " +
                humidity + "% " +
                visibility + " km " +
                sdf.format(date) + " ";
    }
}


class WeatherStation {
    int days;
    ArrayList<WeatherReport> weatherReports;


    public WeatherStation(int n) {
        weatherReports = new ArrayList<WeatherReport>();
        this.days = n;
    }

    public void addMeasurment(float temp, float wind, float hum, float vis, Date date) {

        weatherReports.removeIf(wr -> (wr.date.getTime() + (long) days * 24 * 60 * 60 * 1000) < date.getTime());

        boolean exists = weatherReports.stream()
                .anyMatch(wr ->
                        Math.abs(wr.date.getTime() - date.getTime()) < 150000
                );

        if (!exists) {
            weatherReports.add(new WeatherReport(temp, wind, hum, vis, date));
        }
    }

    public int total() {
        return weatherReports.size();
    }

    public void status(Date from, Date to) {
        ArrayList<WeatherReport> weatherReportsFiltered = weatherReports.stream()
                .filter(wr -> wr.date.getTime() >= from.getTime() && wr.date.getTime() <= to.getTime())
                .sorted((a, b) -> Long.compare(a.date.getTime(), b.date.getTime()))
                .collect(Collectors.toCollection(ArrayList<WeatherReport>::new));

        if (weatherReportsFiltered.isEmpty()) {
            throw new RuntimeException();
        }
        AtomicReference<Float> temp = new AtomicReference<>((float) 0);
        weatherReportsFiltered.forEach((wr) -> {
            temp.updateAndGet(v -> (float) (v + wr.temperature));
            System.out.println(wr);
        });
        System.out.printf("Average temperature: %.2f%n", temp.get() / weatherReportsFiltered.size());


    }


}
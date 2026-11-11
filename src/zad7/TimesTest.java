package zad7;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TimesTest {

    public static void main(String[] args) throws IOException {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}

class UnsupportedFormatException extends Exception {
    public UnsupportedFormatException(String message) {
        super(message);
    }
}

class InvalidTimeException extends Exception {
    public InvalidTimeException(String message) {
        super(message);
    }
}

class Time {
    int hour;
    int minutes;

    public Time(int hour, int minutes) {
        this.hour = hour;
        this.minutes = minutes;
    }

    public String printTime(TimeFormat tf) {
        if (tf.equals(TimeFormat.FORMAT_24)) {
            return convert24();
        }
        return convertAmPm();
    }

    public String convert24() {
        String h = "";
        if (hour < 10) {
            h = " ";
        }
        h += String.valueOf(hour);

        String m = "";
        if (minutes < 10) {
            m = "0";
        }
        m += String.valueOf(minutes);

        return h + ":" + m;

    }

    public String convertAmPm() {
        String stamp = " PM";

        if (hour >= 0 && hour < 12) {
            stamp = " AM";
        }

        String h = "";
        if (hour == 0) {
            h = "12";
        } else if (hour < 10) {
            h = " ";
            h += String.valueOf(hour);
        } else if (hour > 12 && hour < 22) {
            h = " ";
            h += String.valueOf(hour - 12);
        } else if (hour <= 12) {
            h += String.valueOf(hour);
        } else {
            h += String.valueOf(hour - 12);
        }

        String m = "";
        if (minutes < 10) {
            m = "0";
        }
        m += String.valueOf(minutes);

        return h + ":" + m + stamp;

    }

}

class TimeTable implements Comparator<Time> {
    List<Time> times;

    public TimeTable() {
        this.times = new ArrayList<>();
    }

    void readTimes(InputStream inputStream) throws IOException, InvalidTimeException, UnsupportedFormatException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        String line_all;
        while ((line_all = br.readLine()) != null) {
            String[] line_parts = line_all.split("\\s+");

            for (String line : line_parts) {
                int hour;
                int minutes;

                List<String> parts = List.of("-1", "-1");

                if (line.contains(":")) {
                    parts = List.of(line.split(":"));
                } else if (line.contains(".")) {
                    parts = List.of(line.split("\\."));
                } else {
                    throw new UnsupportedFormatException(line);
                }

                hour = Integer.parseInt(parts.get(0));
                minutes = Integer.parseInt(parts.get(1));

                if (hour < 0 || hour > 23 || minutes < 0 || minutes > 59) {
                    throw new InvalidTimeException(line);
                }

                Time t = new Time(hour, minutes);
                times.add(t);
            }

        }
    }

    void writeTimes(OutputStream outputStream, TimeFormat format) {
        PrintWriter pw = new PrintWriter(outputStream);

        List<Time> times_sorted = times.stream().sorted(this).collect(Collectors.toCollection(ArrayList::new));

        times_sorted.stream().map(t -> t.printTime(format)).forEach(pw::println);

        pw.flush();

    }

    @Override
    public int compare(Time o1, Time o2) {
        if (o1.hour == o2.hour) {
            return Integer.compare(o1.minutes, o2.minutes);
        }
        return Integer.compare(o1.hour, o2.hour);
    }

}
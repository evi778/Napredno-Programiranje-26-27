package zad7;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;


enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}


class InvalidTimeException extends Exception {
    public InvalidTimeException(String message) {
        super(message);
    }
}

class UnsupportedFormatException extends Exception {
    public UnsupportedFormatException(String message) {
        super(message);
    }
}

class Time {
    int hour;
    int minute;

    public Time(String time) throws UnsupportedFormatException, InvalidTimeException {
        if (time.contains(":")) {
            String[] parts = time.split(":");
            this.hour = Integer.parseInt(parts[0]);
            this.minute = Integer.parseInt(parts[1]);

            if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                throw new InvalidTimeException("Invalid time exception");
            }
        } else if (time.contains(".")) {
            String[] parts = time.split("\\.");
            this.hour = Integer.parseInt(parts[0]);
            this.minute = Integer.parseInt(parts[1]);

            if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                throw new InvalidTimeException("Invalid time exception");
            }
        } else {
            throw new UnsupportedFormatException("UnsupportedFormatException: " + time);
        }
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    String print(TimeFormat timeFormat) {
        String time = "";
        if (timeFormat == TimeFormat.FORMAT_AMPM) {
            String stamp = "AM";

            int new_hour = this.hour;
            if (new_hour == 0) {
                new_hour = 12;
            } else if (new_hour == 12) {
                stamp = "PM";
            } else if (new_hour > 12) {
                new_hour -= 12;
                stamp = "PM";
            }

            String m = "" + minute;
            if (minute < 10) {
                m = "0" + minute;
            }
            String h = "" + new_hour;
            if (new_hour < 10) {
                h = " " + new_hour;
            }

            time = h + ":" + m + " " + stamp;
        } else if (timeFormat == TimeFormat.FORMAT_24) {
            if (hour < 10) {
                time += " ";
            }
            String m = "" + minute;
            if (minute < 10) {
                m = "0" + minute;
            }
            String h = "" + hour;
            if (hour < 10) {
                h = " " + hour;
            }


            time = h + ":" + m;
        }

        return time;

    }


}

class TimeTable implements Comparator<Time> {

    ArrayList<Time> times;

    public TimeTable() {
        this.times = new ArrayList<>();
    }

    public void readTimes(InputStream in)
            throws InvalidTimeException, UnsupportedFormatException, IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = br.readLine()) != null) {
            for (String part : line.split(" ")) {
                try {
                    times.add(new Time(part.strip()));
                } catch (UnsupportedFormatException | InvalidTimeException e) {
                    System.out.println(e.getMessage());
                }
            }

        }
    }

    public void writeTimes(PrintStream out, TimeFormat timeFormat) {

        PrintWriter pr = new PrintWriter(out);

        times.stream().sorted(this).map(t -> t.print(timeFormat)).forEach(pr::println);

        pr.flush();
    }

    @Override
    public int compare(Time o1, Time o2) {
        if (o1.hour == o2.hour) {
            if (o1.minute == o2.minute) {
                return 0;
            } else if (o1.minute > o2.minute) {
                return 1;
            }
            return -1;
        } else if (o1.hour > o2.hour) {
            return 1;
        }
        return -1;
    }


}

public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException | IOException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

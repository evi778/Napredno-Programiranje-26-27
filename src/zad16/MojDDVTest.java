package zad16;

import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

    }
}

class AmountNotAllowedException extends Exception {
    public AmountNotAllowedException(int value) {
        super("Receipt with amount " + value + " is not allowed to be scanned");
    }
}


class Item {
    int price;
    float tax;

    public Item(int price, String taxType) {
        this.price = price;

        switch (taxType) {
            case "A":
                tax = 0.18f;
                break;
            case "B":
                tax = 0.05f;
                break;
            case "V":
                tax = 0.00f;
                break;
        }
    }

    public double getTax() {
        return 0.15 *tax * price;
    }
}

class Record {
    int id;
    ArrayList<Item> items;
    int total;

    public Record(String full_record) throws AmountNotAllowedException {
        String[] parts = full_record.split("\\s+");
        id = Integer.parseInt(parts[0]);
        items = new ArrayList<Item>();
        total = 0;

        for (int i = 1; i < parts.length; i += 2) {
            int price = Integer.parseInt(parts[i]);
            total += price;
            String taxType = parts[i + 1];
            items.add(new Item(price, taxType));
        }
        if (total > 30000) {
            throw new AmountNotAllowedException(total);
        }
    }

    public int getTotal() {
        return total;
    }

    public int getId() {
        return id;
    }

    public double getTax() {
        return items.stream().mapToDouble(Item::getTax).sum();
    }
}

class MojDDV {

    ArrayList<Record> records;

    public MojDDV() {
        records = new ArrayList<Record>();
    }

    public void readRecords(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        br.lines().forEach(line -> {
            try {
                records.add(new Record(line));
            } catch (AmountNotAllowedException e) {
                System.out.println(e.getMessage());
            }

        });
    }

    public void printTaxReturns(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);

        records.forEach(record -> {
            String s = String.format("%d %d %.2f",record.getId(),record.getTotal(),record.getTax());
            out.println(s);
        });
        pw.flush();
    }
}

package laboratoriski;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

class InvalidProductException extends Exception {
    public InvalidProductException(String line) {
        super("Invalid product: " + line);
    }
}

class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException(String category) {
        super("Category " + category + " not found");
    }
}

abstract class Product implements Comparable<Product> {
    // TODO: id, name, category, basePrice
    int id;
    String name;
    String category;
    double basePrice;

    public Product(int id, String name, String category, double basePrice) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.basePrice = basePrice;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public abstract double getFinalPrice();

    public abstract char getTypeCode();

    public static Product createProduct(String line) throws InvalidProductException {
        String[] parts = line.split(";", -1);

        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        if (parts.length != 6 || (!parts[0].equals("P") && !parts[0].equals("D"))) {
            throw new InvalidProductException(line);
        }


        Product p;
        try {
            String TYPE = parts[0];
            int id = Integer.parseInt(parts[1]);
            String name = parts[2];
            String category = parts[3];
            double basePrice = Double.parseDouble(parts[4]);

            if (basePrice < 0) {
                throw new InvalidProductException(line);
            }

            if (TYPE.equals("P")) {
                double weight = Double.parseDouble(parts[5]);

                if (weight <= 0) {
                    throw new InvalidProductException(line);
                }

                p = new PhysicalProduct(id, name, category, basePrice, weight);
            } else {
                int sizeMb = Integer.parseInt(parts[5]);

                if (sizeMb <= 0) {
                    throw new InvalidProductException(line);
                }

                p = new DigitalProduct(id, name, category, basePrice, sizeMb);
            }
        } catch (Exception e) {
            throw new InvalidProductException(line);
        }


        return p;
    }

    @Override
    public int compareTo(Product other) {
        if (this.getFinalPrice() == other.getFinalPrice()) {
            if (this.name.equals(other.name)) {
                return Integer.compare(this.id, other.id);
            }
            return String.CASE_INSENSITIVE_ORDER.compare(this.name, other.name);
        }
        return Double.compare(this.getFinalPrice(), other.getFinalPrice());
    }

    @Override
    public String toString() {
        return String.format("%c %-5d %-15s %-12s %10.2f", this.getTypeCode(), id, name, category, this.getFinalPrice());
    }
}

class PhysicalProduct extends Product {
    double weight;

    public PhysicalProduct(int id, String name, String category, double basePrice, double weight) {
        super(id, name, category, basePrice);
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public double getShippingCost() {
        if (this.weight <= 1) {
            return 60;
        }
        return 60 + 30 * (weight - 1);
    }

    @Override
    public double getFinalPrice() {
        return basePrice + this.getShippingCost();
    }

    @Override
    public char getTypeCode() {
        return 'P';
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | %.2f kg", weight);
    }
}

class DigitalProduct extends Product {
    int sizeMb;

    public DigitalProduct(int id, String name, String category, double basePrice, int sizeMb) {
        super(id, name, category, basePrice);
        this.sizeMb = sizeMb;
    }

    public int getSizeMb() {
        return sizeMb;
    }

    @Override
    public double getFinalPrice() {
        return basePrice * 0.9;
    }

    @Override
    public char getTypeCode() {
        return 'D';
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | %d MB", sizeMb);
    }
}

class Store {
    ArrayList<Product> products;
    int invalid = 0;

    public Store() {
        products = new ArrayList<>();
    }

    public int readProducts(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        int valid = 0;
        String line;

        while ((line = br.readLine()) != null) {
            if (line.isBlank()) {
                continue;
            }
            try {
                Product p = Product.createProduct(line);
                products.add(p);
                valid++;
            } catch (InvalidProductException e) {
                invalid++;
            }
        }

        return valid;
    }

    public int getInvalidLines() {
        return invalid;
    }

    public void printSorted(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);

        products.stream()
                .sorted()
                .forEach(pw::println);

        pw.flush();
    }

    public void printByCategory(OutputStream os, String category) throws CategoryNotFoundException {
        PrintWriter pw = new PrintWriter(os);


        ArrayList<Product> filtered = products.stream()
                .filter(p -> p.getCategory().equals(category))
                .sorted(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(Product::getFinalPrice, Comparator.reverseOrder())
                        .thenComparing(Product::getId))
                .collect(Collectors.toCollection(ArrayList::new));

        if (filtered.isEmpty()) {
            throw new CategoryNotFoundException(category);
        }

        filtered.forEach(pw::println);

        pw.flush();
    }

    public void printStatistics(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);

        int n = products.size();

        if (n == 0) {
            System.out.println("No products");
            return;
        }
        int p = (int) products.stream().filter(pr -> pr.getTypeCode() == 'P').count();
        int d = (int) products.stream().filter(pr -> pr.getTypeCode() == 'D').count();

        Product p_cheapest = products.stream().min(Comparator.comparing(Product::getFinalPrice)).get();
        Product p_expensive = products.stream().max(Comparator.comparing(Product::getFinalPrice)).get();
        double average = products.stream().mapToDouble(Product::getFinalPrice).sum() / n;

        pw.printf("Total: %d (physical: %d, digital: %d)\n" +
                        "Cheapest: %s\n" +
                        "Most expensive: %s\n" +
                        "Average price: %.2f\n",
                n, p, d, p_cheapest, p_expensive, average);

        pw.flush();
    }
}

public class StoreTest {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder productLines = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null && !line.trim().equals("COMMANDS")) {
            productLines.append(line).append("\n");
        }

        Store store = new Store();
        int read = store.readProducts(
                new ByteArrayInputStream(productLines.toString().getBytes(StandardCharsets.UTF_8)));
        System.out.println("Read products: " + read);
        System.out.println("Invalid lines: " + store.getInvalidLines());

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\s+");
            System.out.println("=== " + line + " ===");
            switch (parts[0]) {
                case "SORTED":
                    store.printSorted(System.out);
                    break;
                case "CATEGORY":
                    try {
                        store.printByCategory(System.out, parts[1]);
                    } catch (CategoryNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "STATS":
                    store.printStatistics(System.out);
                    break;
                default:
                    System.out.println("Unknown command");
            }
        }
    }
}

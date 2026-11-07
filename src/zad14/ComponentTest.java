package zad14;

import java.util.*;
import java.util.stream.Collectors;

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if (what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}

// вашиот код овде
class Component {
    String color;
    int weight;
    ArrayList<Component> components;

    public int getWeight() {
        return weight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
        components = new ArrayList<>();
    }

    void addComponent(Component component) {
        components.add(component);
        components = components.stream().
                sorted(Comparator.comparing(Component::getWeight).thenComparing(Component::getColor)).
                collect(Collectors.toCollection(ArrayList::new));
    }

    void changeColor(int weight, String color) {
        if (this.weight < weight) {
            this.color = color;
        }
        components.forEach(component -> component.changeColor(weight, color));
    }


    public String toString(String prefix) {
        String s = weight + ":" + color + "\n";
        prefix+="---";
        for (Component component : components) {
            s += prefix + component.toString(prefix);
        }
        return s;
    }
}

class InvalidPositionException extends Exception {
    public InvalidPositionException(int pos) {
        super("Invalid position " + pos + ", alredy taken!");
    }
}

class Window {
    String name;
    ArrayList<Component> components;
    ArrayList<Integer> positions;

    public Window(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        this.positions = new ArrayList<>();
    }

    void addComponent(int position, Component component) throws InvalidPositionException {

        Optional<Integer> found = positions.stream().filter(pos -> pos == position).findFirst();

        if(found.isPresent()) {
            throw new InvalidPositionException(position);
        }

        components.add(component);
        positions.add(position);

        for (int i = 0; i < positions.size(); i++) {
            for (int j = i + 1; j < positions.size(); j++) {
                if (positions.get(i) >= (positions.get(j))) {

                    int temp = positions.get(i);
                    positions.set(i, positions.get(j));
                    positions.set(j, temp);

                    Component tempC = components.get(i);
                    components.set(i, components.get(j));
                    components.set(j, tempC);
                    break;
                }
            }
        }
    }

    void changeColor(int weight, String color) {
        components.forEach(component -> component.changeColor(weight, color));
    }

    void swichComponents(int pos1, int pos2) {

        int i = positions.indexOf(pos1);
        int j = positions.indexOf(pos2);

        int temp = positions.get(i);
        positions.set(i, positions.get(j));
        positions.set(j, temp);

        Component tempC = components.get(i);
        components.set(i, components.get(j));
        components.set(j, tempC);
    }

    @Override
    public String toString() {

        String s ="WINDOW "+ name + '\n';
        for(int i=1; i<=components.size(); i++) {
            s += i + ":" + components.get(i-1).toString("");
        }

         return s;

    }
}
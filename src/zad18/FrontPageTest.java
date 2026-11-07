package zad18;


import java.util.*;
import java.util.stream.Collectors;

public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for (Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch (CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
// Vasiot kod ovde

class Category implements Comparable<Category> {
    String name;

    public Category(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Category o) {
        return name.compareTo(o.name);
    }
}

class NewsItem {
    String title;
    Date date;
    Category category;

    public NewsItem(String title, Date date, Category category) {
        this.title = title;
        this.date = date;
        this.category = category;
    }

    String getTeaser() {
        return null;
    }
}

class TextNewsItem extends NewsItem {
    private String text;

    public TextNewsItem(String title, Date date, Category category, String text) {
        super(title, date, category);
        this.text = text;
    }

    @Override
    String getTeaser() {
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        return title + "\n"
                + (int) (now.getTime() - date.getTime()) / 60000 + "\n"
                + text.substring(0, Math.min(text.length(), 80)) + "\n";
    }
}

class MediaNewsItem extends NewsItem {
    private String url;
    private int views;

    public MediaNewsItem(String title, Date date, Category category, String url, int views) {
        super(title, date, category);
        this.url = url;
        this.views = views;
    }

    @Override
    String getTeaser() {
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        return title + "\n"
                + (int) (now.getTime() - date.getTime()) / 60000 + "\n"
                + url + "\n"
                + views + "\n";
    }
}

class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException(String category) {
        super("Category " + category + " was not found");
    }
}

class FrontPage {
    ArrayList<NewsItem> newsItems;
    ArrayList<Category> categories;

    FrontPage(Category[] categories) {
        newsItems = new ArrayList<NewsItem>();
        this.categories = new ArrayList<Category>(List.of(categories));
    }

    public void addNewsItem(NewsItem newsItem) {
        newsItems.add(newsItem);
    }

    public List<NewsItem> listByCategory(Category category) {
        return newsItems.stream().filter(item -> item.category.equals(category)).collect(Collectors.toList());

    }

    public List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {
        categories.stream().filter(c -> c.name.equals(category)).findFirst().orElseThrow(() -> new CategoryNotFoundException(category));

        return newsItems.stream().filter(c -> c.category.name.equals(category)).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return newsItems.stream().map(NewsItem::getTeaser).collect(Collectors.joining(""));
    }
}
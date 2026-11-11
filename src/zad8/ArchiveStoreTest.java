package zad8;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        Date date = new Date(113, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();
            Date dateToOpen = new Date(date.getTime() + (days * 24 * 60 * 60 * 1000));
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while (scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch (NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}

// вашиот код овде

abstract class Archive {
    int id;
    Date dateArchived;

    public Archive(int id) {
        this.id = id;
    }

    public void setDateArchived(Date dateArchived) {
        this.dateArchived = dateArchived;
    }

    public String open(Date dateOpened) {
        return "";
    }

}

class LockedArchive extends Archive {
    Date dateToOpen;

    public LockedArchive(int id, Date dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }

    @Override
    public String open(Date dateOpened) {
        if (dateToOpen.after(dateOpened)) {
            return String.format("Item %d cannot be opened before %s", id, dateToOpen);
        }
        return String.format("Item %d opened at %s", id, dateOpened);
    }
}

class SpecialArchive extends Archive {
    int maxOpen;
    int oppened;

    public SpecialArchive(int id, int maxOpen) {
        super(id);
        this.maxOpen = maxOpen;
        oppened = 0;
    }

    @Override
    public String open(Date dateOpened) {
        if (oppened >= maxOpen) {
            return String.format("Item %d cannot be opened more than %d times", id, maxOpen);
        }
        oppened++;
        return String.format("Item %d opened at %s", id, dateOpened);
    }
}

class ArchiveStore {
    List<Archive> archiveList;
    List<String> logs;

    public ArchiveStore() {
        archiveList = new ArrayList<>();
        logs = new ArrayList<>();
    }

    public void archiveItem(Archive item, Date date) {
        item.setDateArchived(date);
        archiveList.add(item);

        logs.add(String.format("Item %d archived at %s", item.id, date));
    }

    public void openItem(int id, Date date) throws NonExistingItemException {
        Archive archive = archiveList.stream().filter(a -> a.id == id).findAny().orElse(null);

        if (archive == null) {
            throw new NonExistingItemException(id);
        }

        logs.add(archive.open(date));

    }

    public String getLog() {
        return String.join("\n", logs);
    }
}


class NonExistingItemException extends Exception {
    public NonExistingItemException(int id) {
        super(String.format("Item with id %d doesn't exist", id));
    }
}
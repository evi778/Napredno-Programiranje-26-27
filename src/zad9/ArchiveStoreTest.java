package zad9;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;


public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
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
class NonExistingItemException extends Exception {

    public NonExistingItemException(int id) {
        super(String.format("Item with id %d doesn't exist", id));
    }
}

abstract class Archive {
    int id;
    LocalDate dateArchived;

    public Archive(int id) {
        this.id = id;
        this.dateArchived = null;
    }

    public LocalDate getDateArchived() {
        return dateArchived;
    }

    public String setDateArchived(LocalDate dateArchived) {
        this.dateArchived = dateArchived;
        return "Item " + id + " archived at " + dateArchived.toString();
    }

    public String open(LocalDate date) {
        return "";
    }
}

class LockedArchive extends Archive {
    LocalDate dateToOpen;

    public LockedArchive(int id, LocalDate dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }

    @Override
    public String open(LocalDate date) {
        if (date.compareTo(dateToOpen) <= 0) {
            return "Item " + id + " cannot be opened before " + dateToOpen;
        }
        return "Item " + id + " opened at " + date;

    }

}

class SpecialArchive extends Archive {
    int maxOpen;
    int timesOpened;

    public SpecialArchive(int id, int maxOpen) {
        super(id);
        this.maxOpen = maxOpen;
        this.timesOpened = 0;
    }

    @Override
    public String open(LocalDate date) {
        if (timesOpened == maxOpen) {
            return "Item " + id + " cannot be opened more than " + maxOpen + " times";
        }
        timesOpened++;
        return "Item " + id + " opened at " + date;
    }

}


class ArchiveStore {
    ArrayList<Archive> archives;
    ArrayList<String> logs;

    public ArchiveStore() {
        archives = new ArrayList<>();
        logs = new ArrayList<>();
    }

    public void archiveItem(Archive item, LocalDate date) {
        String log = item.setDateArchived(date);
        archives.add(item);
        logs.add(log);
    }

    public void openItem(int id, LocalDate date) throws NonExistingItemException {
        String log = archives.stream().filter(archive -> archive.id == id).
                findFirst().
                orElseThrow(() -> new NonExistingItemException(id)).open(date);
        logs.add(log);
    }

    public String getLog() {
        return String.join("\n", logs);
    }
}




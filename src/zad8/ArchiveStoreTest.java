package zad8;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;


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
            Date dateToOpen = new Date(date.getTime() + (days * 24 * 60
                    * 60 * 1000));
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
    Date dateArchived;

    public Archive(int id) {
        this.id = id;
        this.dateArchived = null;
    }

    public Date getDateArchived() {
        return dateArchived;
    }

    public String setDateArchived(Date dateArchived) {
        this.dateArchived = dateArchived;
        return "Item " + id + " archived at " + dateArchived.toString();
    }

    public String open(Date date) {
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
    public String open(Date date) {
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
    public String open(Date date) {
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

    public void archiveItem(Archive item, Date date) {
        String log = item.setDateArchived(date);
        archives.add(item);
        logs.add(log);
    }

    public void openItem(int id, Date date) throws NonExistingItemException {
        String log = archives.stream().filter(archive -> archive.id == id).
                findFirst().
                orElseThrow(() -> new NonExistingItemException(id)).open(date);
        logs.add(log);
    }

    public String getLog() {
        return String.join("\n", logs);
    }
}




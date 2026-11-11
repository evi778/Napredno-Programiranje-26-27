package zad3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Scanner;

public class FileSystemTest {

    public static Folder readFolder(Scanner sc) {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < totalFiles; i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String[] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args) {

        //file reading from input

        Scanner sc = new Scanner(System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());


    }
}

interface IFile {
    String getFileName();

    long getFileSize();

    String getFileInfo(String prefix);

    void sortBySize();

    long findLargestFile();

    String fileType();

}

class File implements IFile {
    String fileName;
    long size;

    public File(String fileName, long size) {
        this.fileName = fileName;
        this.size = size;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public long getFileSize() {
        return size;
    }

    @Override
    public String getFileInfo(String prefix) {
        return String.format("%sFile name: %10s File size: %10d", prefix, fileName, size);
    }

    @Override
    public void sortBySize() {

    }

    @Override
    public long findLargestFile() {
        return 0;
    }

    @Override
    public String fileType() {
        return "File";
    }

}

class Folder implements IFile {
    String fileName;
    List<IFile> files;

    public Folder(String fileName) {
        this.fileName = fileName;
        files = new ArrayList<>();
    }

    void addFile(IFile file) throws FileNameExistsException {
        if (files.stream().anyMatch(f -> f.getFileName().equals(file.getFileName()))) {
            throw new FileNameExistsException(file.getFileName(), fileName);
        }
        files.add(file);
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public long getFileSize() {
        return files.stream().mapToLong(IFile::getFileSize).sum();
    }

    @Override
    public String getFileInfo(String prefix) {
        String s = (String.format("%sFolder name: %10s Folder size: %10d", prefix, fileName, getFileSize()));
        prefix += "    ";
        String finalPrefix = prefix;
        s += files.stream().map(f -> "\n" + f.getFileInfo(finalPrefix)).collect(Collectors.joining());
        return s;
    }

    @Override
    public void sortBySize() {
        files = files.stream().sorted(Comparator.comparing(IFile::getFileSize)).collect(Collectors.toCollection(ArrayList::new));
        files.stream().filter(f -> f.fileType().equals("Folder")).forEach(IFile::sortBySize);
    }

    @Override
    public long findLargestFile() {

        return Math.max(files.stream().filter(f -> f.fileType().equals("File"))
                        .max(Comparator.comparing(IFile::getFileSize))
                        .map(IFile::getFileSize)
                        .orElse(0L),

                files.stream().filter(f -> f.fileType().equals("Folder"))
                        .max(Comparator.comparing(IFile::findLargestFile))
                        .map(IFile::findLargestFile)
                        .orElse(0L));
    }

    @Override
    public String fileType() {
        return "Folder";
    }

}

class FileSystem {
    Folder root;

    public FileSystem() {
        this.root = new Folder("root");
    }

    void addFile(IFile file) throws FileNameExistsException {
        root.addFile(file);
    }

    long findLargestFile() {
        return root.findLargestFile();
    }

    void sortBySize() {
        root.sortBySize();
    }

    @Override
    public String toString() {
        return root.getFileInfo("") + "\n";
    }
}


class FileNameExistsException extends Exception {
    public FileNameExistsException(String name, String folder) {
        super("There is already a file named " + name + " in the folder " + folder);
    }
}

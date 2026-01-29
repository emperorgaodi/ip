import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    // Load tasks from file - returns empty list if file does not exist
    public ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            // file does not exist - Ok for first run
            return tasks;
        }

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Task task = parseLine(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
            scanner.close();
        } catch (IOException e) {
            // File does not exist - Ok if running program for the first time
            System.out.println("Error reading file: " + e.getMessage());
        }

        return tasks;
    }

    public void saveTasks(ArrayList<Task> tasks) {
        try {
            // Create data directory if it does not exist
            File dataDir = new File("Data");
            if (!dataDir.exists()) {
                dataDir.mkdir();
            }

            FileWriter writer = new FileWriter(filePath);
            for (Task task : tasks) {
                writer.write(task.toFileFormat() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    // parse line from file into Task object
    private Task parseLine(String line) {
        try {
            String[] parts = line.split("\\|");

            if (parts.length < 3) {
                return null; // skip invalid lines
            }

            String type = parts[0];
            boolean isDone = parts[1].equals("1");
            String description = parts[2];

            Task task = null;

            if (type.equals("T") && parts.length == 3) {
                task = new ToDo(description);
            } else if (type.equals("D") && parts.length == 4) {
                task = new Deadline(description, parts[3]);
            } else if (type.equals("E") && parts.length == 5) {
                task = new Event(description, parts[3], parts[4]);
            }

            if (task != null && isDone) {
                task.markAsDone();
            }

            return task;

        } catch (Exception e) {
            return null; // if anything goes wrong, skip this line
        }
    }
}

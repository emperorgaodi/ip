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
                System.out.println("Warning: Skipping invalid line: " + line);
                return null; // skip invalid lines
            }

            String type = parts[0];
            boolean isDone = parts[1].equals("1");
            String description = parts[2];

            Task task = parseTaskByType(type, parts, description);

            if (task != null && isDone) {
                task.markAsDone();
            }

            return task;

        } catch (DarwinException e) {
            System.out.println("Warning: Skipping task - " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Warning: Skipping invalid line");
            return null;
        }
    }

    private Task parseTaskByType(String type, String[] parts, String description) throws DarwinException {
        switch (type) {
            case "T":
                return parseTodoLine(parts, description);
            case "D":
                return parseDeadlineLine(parts, description);
            case "E":
                return parseEventLine(parts, description);
            default:
                throw new DarwinException("Unknown task type in file: " + type);
        }
    }

    private Task parseTodoLine(String[] parts, String description) throws DarwinException {
        if (parts.length != 3) {
            throw new DarwinException("Invalid todo format in file");
        }
        return new ToDo(description);
    }

    private Task parseDeadlineLine(String[] parts, String description) throws DarwinException {
        if (parts.length != 4) {
            throw new DarwinException("Invalid deadline format in file");
        }

        String dateString = parts[3].trim();
        return new Deadline(description, dateString); // Will throw if date invalid
    }

    private Task parseEventLine(String[] parts, String description) throws DarwinException {
        if (parts.length != 5) {
            throw new DarwinException("Invalid event format in file");
        }

        String from = parts[3].trim();
        String to = parts[4].trim();
        return new Event(description, from, to); // Will throw if dates invalid
    }
}

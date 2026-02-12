package darwin.storage;

import darwin.DarwinException;
import darwin.task.Deadline;
import darwin.task.Event;
import darwin.task.Task;
import darwin.task.ToDo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents a storage handler for task persistence. A <code>Storage</code> object
 * manages the loading of tasks from a file on disk and the saving of tasks back to the file,
 * ensuring data persistence across application sessions.
 */
public class Storage {

    private final String filePath;

    /**
     * Constructs a Storage object with the specified file path for task storage.
     *
     * @param filePath The file path where tasks will be loaded from and saved to.
     *                 The file is created if it doesn't exist.
     */
    public Storage(String filePath) {
        assert filePath != null : "File path cannot be null";
        assert !filePath.trim().isEmpty() : "File path cannot be empty";

        this.filePath = filePath;
    }

    /**
     * Loads tasks from the storage file. If the file doesn't exist, returns an empty list.
     * Parses each line of the file into Task objects, handling any parsing errors gracefully.
     *
     * @return An ArrayList containing all tasks successfully loaded from the file.
     *         Returns an empty list if the file doesn't exist or contains no valid tasks.
     */
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

    /**
     * Saves the current list of tasks to the storage file. Creates the data directory
     * if it doesn't exist, and overwrites the existing file with the current task data.
     *
     * @param tasks The ArrayList of tasks to be saved to the file.
     */
    public void saveTasks(ArrayList<Task> tasks) {
        assert tasks != null : "Tasks list cannot be null when saving";
        assert filePath != null : "File path must be initialized before saving";

        try {
            // Create data directory if it does not exist
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                boolean created = dataDir.mkdir();
                assert created : "Failed to create data directory";
            }

            FileWriter writer = new FileWriter(filePath);
            for (Task task : tasks) {
                assert task != null : "Task in list cannot be null when saving";
                String fileFormat = task.toFileFormat();
                assert fileFormat != null : "Task.toFileFormat() should not return null";

                writer.write(fileFormat + "\n");
            }
            writer.close();

            File savedFile = new File(filePath);
            assert savedFile.exists() : "File should exist after saving: " + filePath;
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Parses a single line from the storage file into a Task object.
     * Handles different task types (Todo, Deadline, Event) and their specific formats.
     *
     * @param line A single line from the storage file to parse.
     * @return A Task object parsed from the line, or null if the line is invalid.
     */
    private Task parseLine(String line) {
        try {
            String[] parts = line.split("\\|");

            if (parts.length < 3) {
                System.out.println("Warning: Skipping invalid line: " + line);
                return null; // skip invalid lines
            }

            String type = parts[0].trim();
            boolean isDone = parts[1].trim().equals("1");
            String description = parts[2].trim();

            Task task = parseTaskByType(type, parts, description);

            if (task != null && isDone) {
                task.markAsDone();
            }

            return task;

        } catch (DarwinException e) {
            System.out.println("Warning: Skipping task - " + e.getMessage());
            return null;
        } catch (RuntimeException e) {
            System.out.println("Warning: Skipping invalid line");
            return null;
        }
    }

    /**
     * Routes parsing to the appropriate task type parser based on the type code.
     *
     * @param type The task type code ("T" for Todo, "D" for Deadline, "E" for Event).
     * @param parts The split parts of the file line.
     * @param description The task description extracted from the line.
     * @return A Task object of the appropriate type.
     * @throws DarwinException If the task type is unknown or the line format is invalid.
     */
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

    /**
     * Parses a todo task line from the storage file.
     *
     * @param parts The split parts of the file line.
     * @param description The task description.
     * @return A Todo task object.
     * @throws DarwinException If the line doesn't have exactly 3 parts.
     */
    private Task parseTodoLine(String[] parts, String description) throws DarwinException {
        if (parts.length != 3) {
            throw new DarwinException("Invalid todo format in file");
        }
        return new ToDo(description);
    }

    /**
     * Parses a deadline task line from the storage file.
     *
     * @param parts The split parts of the file line.
     * @param description The task description.
     * @return A Deadline task object.
     * @throws DarwinException If the line doesn't have exactly 4 parts or the date is invalid.
     */
    private Task parseDeadlineLine(String[] parts, String description) throws DarwinException {
        if (parts.length != 4) {
            throw new DarwinException("Invalid deadline format in file");
        }

        String dateString = parts[3].trim();
        return new Deadline(description, dateString);
    }

    /**
     * Parses an event task line from the storage file.
     *
     * @param parts The split parts of the file line.
     * @param description The task description.
     * @return An Event task object.
     * @throws DarwinException If the line doesn't have exactly 5 parts or the dates are invalid.
     */
    private Task parseEventLine(String[] parts, String description) throws DarwinException {
        if (parts.length != 5) {
            throw new DarwinException("Invalid event format in file");
        }

        String from = parts[3].trim();
        String to = parts[4].trim();
        return new Event(description, from, to);
    }
}

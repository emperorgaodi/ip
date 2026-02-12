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

    // ============ FILE FORMAT CONSTANTS ============
    private static final String DELIMITER = "\\|";
    private static final String DATA_DIRECTORY = "data";

    // ============ TASK TYPE CONSTANTS ============
    private static final String TASK_TYPE_TODO = "T";
    private static final String TASK_TYPE_DEADLINE = "D";
    private static final String TASK_TYPE_EVENT = "E";
    private static final String STATUS_DONE = "1";

    // ============ INDEX CONSTANTS ============
    private static final int INDEX_TYPE = 0;
    private static final int INDEX_STATUS = 1;
    private static final int INDEX_DESCRIPTION = 2;
    private static final int INDEX_DEADLINE_DATE = 3;
    private static final int INDEX_EVENT_FROM = 3;
    private static final int INDEX_EVENT_TO = 4;

    // ============ VALID PART LENGTHS ============
    private static final int SHORTEST_POSIBLE_TASK_LENGTH = 3;
    private static final int TODO_PARTS_LENGTH = 3;
    private static final int DEADLINE_PARTS_LENGTH = 4;
    private static final int EVENT_PARTS_LENGTH = 5;


    private final String filePath;

    /**
     * Constructs a Storage object with the specified file path for task storage.
     *
     * @param filePath The file path where tasks will be loaded from and saved to.
     *                 The file is created if it doesn't exist.
     */
    public Storage(String filePath) {
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
        if (!isFileExists()) {
            return new ArrayList<>();
        }

        return readTasksFromFile();
    }

    // helper functions for load tasks - start
    private boolean isFileExists() {
        File file = new File(filePath);
        return file.exists();
    }

    private ArrayList<Task> readTasksFromFile() {
        ArrayList<Task> tasks = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                addTaskIfValid(line, tasks);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return tasks;
    }

    private void addTaskIfValid(String line, ArrayList<Task> tasks) {
        Task task = parseLine(line);
        if (isTaskValid(task)) {
            tasks.add(task);
        }
    }

    private boolean isTaskValid(Task task) {
        return task != null;
    }
    // helper functions for load tasks - end

    /**
     * Saves the current list of tasks to the storage file. Creates the data directory
     * if it doesn't exist, and overwrites the existing file with the current task data.
     *
     * @param tasks The ArrayList of tasks to be saved to the file.
     */
    public void saveTasks(ArrayList<Task> tasks) {
        try {
            // Create data directory if it does not exist
            File dataDir = new File(DATA_DIRECTORY);
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

    /**
     * Parses a single line from the storage file into a Task object.
     * Handles different task types (Todo, Deadline, Event) and their specific formats.
     *
     * @param line A single line from the storage file to parse.
     * @return A Task object parsed from the line, or null if the line is invalid.
     */
    private Task parseLine(String line) {
        try {
            String[] parts = line.split(DELIMITER);

            if (parts.length < SHORTEST_POSIBLE_TASK_LENGTH) {
                System.out.println("Warning: Skipping invalid line: " + line);
                return null; // skip invalid lines
            }

            String type = parts[INDEX_TYPE].trim();
            boolean isDone = parts[INDEX_STATUS].trim().equals(STATUS_DONE);
            String description = parts[INDEX_DESCRIPTION].trim();

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
            case TASK_TYPE_TODO:
                return parseTodoLine(parts, description);
            case TASK_TYPE_DEADLINE:
                return parseDeadlineLine(parts, description);
            case TASK_TYPE_EVENT:
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
        if (parts.length != TODO_PARTS_LENGTH) {
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
        if (parts.length != DEADLINE_PARTS_LENGTH) {
            throw new DarwinException("Invalid deadline format in file");
        }

        String dateString = parts[INDEX_DEADLINE_DATE].trim();
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
        if (parts.length != EVENT_PARTS_LENGTH) {
            throw new DarwinException("Invalid event format in file");
        }

        String from = parts[INDEX_EVENT_FROM].trim();
        String to = parts[INDEX_EVENT_TO].trim();
        return new Event(description, from, to);
    }
}

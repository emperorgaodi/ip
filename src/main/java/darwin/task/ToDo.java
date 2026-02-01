package darwin.task;

/**
 * Represents a simple task without any date or time constraints. A <code>ToDo</code> object
 * is the simplest type of task, containing only a description and completion status.
 */
public class ToDo extends Task {

    /**
     * Constructs a new ToDo task with the specified description.
     *
     * @param description The text description of the todo task.
     */
    public ToDo(String description) {
        super(description);
    }

    /**
     * Returns a string representation of the todo task, including its type indicator [T]
     * and status and description.
     *
     * @return A string in the format "[T][status] description".
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Returns a string representation of the todo task suitable for file storage.
     * The format is: "T | status | description"
     *
     * @return A string in file format with pipe-separated values.
     */
    @Override
    public String toFileFormat() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }
}
package darwin.task;

/**
 * Represents a generic task in the Darwin application. A <code>Task</code> object
 * contains a description and a completion status. This is the base class for all
 * specific task types (ToDo, Deadline, Event).
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a new Task with the specified description.
     * The task is initially marked as not done.
     *
     * @param description The text description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon representing whether the task is done.
     * Returns "X" if the task is done, or a space " " if not done.
     *
     * @return A string containing the status icon ("X" or " ").
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done darwin.task with X
    }

    /**
     * Marks this task as completed by setting its done status to true.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not completed by setting its done status to false.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Checks whether this task is marked as done.
     *
     * @return true if the task is done, false otherwise.
     */
    public boolean isDone() {
        return this.isDone;
    }

    /**
     * Returns a string representation of the task, including its status icon
     * and description.
     *
     * @return A string in the format "[status] description".
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /**
     * Returns a string representation of the task suitable for file storage.
     * This method is intended to be overridden by subclasses.
     *
     * @return An empty string by default; subclasses should provide specific format.
     */
    public String toFileFormat() {
        return "";
    }
}
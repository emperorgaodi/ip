package darwin.task;

/**
 * Represents a generic task in the Darwin application. A <code>Task</code> object
 * contains a description and a completion status. This is the base class for all
 * specific task types (ToDo, Deadline, Event).
 */
public class Task {

    private String description;
    private boolean isDone;

    /**
     * Constructs a new Task with the specified description.
     * The task is initially marked as not done.
     *
     * @param description The text description of the task.
     */
    public Task(String description) {
        assert description != null : "Task description cannot be null";

        this.description = description;
        this.isDone = false;

        assert !this.isDone : "New task should not be marked as done";
    }

    /**
     * Returns the description of the task.
     *
     * @return The task description.
     */
    public String getDescription() {
        return description;
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
        boolean wasDone = this.isDone;
        this.isDone = true;

        assert this.isDone : "Task should be marked as done after markAsDone()";
        assert this.isDone != wasDone :
            "Task status should change from false to true, was: " + wasDone;
    }

    /**
     * Marks this task as not completed by setting its done status to false.
     */
    public void markAsNotDone() {
        boolean wasDone = this.isDone;
        this.isDone = false;

        assert !this.isDone : "Task should be marked as not done after markAsNotDone()";
        assert this.isDone != wasDone :
                "Task status should change from true to false, was: " + wasDone;
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
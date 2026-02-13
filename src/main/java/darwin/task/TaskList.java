package darwin.task;

import java.util.ArrayList;
import darwin.DarwinException;

/**
 * Represents a collection of tasks with management operations. A <code>TaskList</code> object
 * stores tasks in an ArrayList and provides methods for adding, deleting, marking,
 * and retrieving tasks, with bounds checking and error handling.
 */
public class TaskList {

    private static final int MAX_TASKS = 100;
    private final ArrayList<Task> tasks;

    /**
     * Constructs an empty TaskList with no initial tasks.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList initialized with the provided list of tasks.
     *
     * @param tasks An ArrayList of Task objects to initialize the list with.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a new task to the task list, if the list is not at maximum capacity.
     *
     * @param task The Task object to add to the list.
     * @throws DarwinException If the task list has reached its maximum capacity (100 tasks).
     */
    public void addTask(Task task) throws DarwinException {
        if (tasks.size() >= MAX_TASKS) {
            throw new DarwinException(" Task list is full! Cannot add more tasks.");
        }
        tasks.add(task);
    }

    /**
     * Deletes a task from the task list at the specified position (1-based indexing).
     * Returns the deleted task for confirmation.
     *
     * @param taskNumber The position of the task to delete (starting from 1).
     * @return The Task object that was removed from the list.
     * @throws DarwinException If the task number is invalid (out of bounds).
     */
    public Task deleteTask(int taskNumber) throws DarwinException {
        if (!isValidTaskNumber(taskNumber)) {
            throw new DarwinException(" Task list is full! Cannot add more tasks.");
        }
        return tasks.remove(taskNumber - 1);
    }

    /**
     * Finds all tasks whose description contains the given keyword (case-insensitive).
     *
     * @param keyword The search term to look for in task descriptions.
     * @return An ArrayList of tasks that match the search criteria.
     */
    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        String searchTerm = keyword.toLowerCase();

        for (Task task : tasks) {
            boolean isMatch = taskContainsKeyword(task, searchTerm);
            if (isMatch) {
                matchingTasks.add(task);
            }
        }

        return matchingTasks;
    }

    private boolean taskContainsKeyword(Task task, String searchTerm) {
        String taskDescription = task.getDescription().toLowerCase();
        return taskDescription.contains(searchTerm);
    }

    /**
     * Marks or unmarks a task at the specified position as done or not done.
     *
     * @param taskNumber The position of the task to mark (starting from 1).
     * @param isDone true to mark the task as done, false to mark it as not done.
     * @throws DarwinException If the task number is invalid (out of bounds).
     */
    public void markTask(int taskNumber, boolean isDone) throws DarwinException {
        if (!isValidTaskNumber(taskNumber)) {
            throw new DarwinException("Invalid task number.");
        }

        Task task = tasks.get(taskNumber - 1);
        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
    }

    /**
     * Returns the internal ArrayList of tasks. Note: This returns the actual list,
     * not a copy, so modifications will affect the original.
     *
     * @return The ArrayList containing all tasks.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Returns the current number of tasks in the list.
     *
     * @return The count of tasks in the list.
     */
    public int getTaskCount() {
        return tasks.size();
    }

    /**
     * Retrieves a task at the specified position without removing it (1-based indexing).
     *
     * @param taskNumber The position of the task to retrieve (starting from 1).
     * @return The Task object at the specified position.
     * @throws DarwinException If the task number is invalid (out of bounds).
     */
    public Task getTask(int taskNumber) throws DarwinException {
        if (!isValidTaskNumber(taskNumber)) {
            throw new DarwinException("Invalid task number.");
        }
        return tasks.get(taskNumber - 1);
    }

    /**
     * Checks if the specified task number is valid for the current list.
     * A valid task number is between 1 and the current list size (inclusive).
     *
     * @param taskNumber The task number to validate.
     * @return true if the task number is valid, false otherwise.
     */
    public boolean isValidTaskNumber(int taskNumber) {
        return taskNumber >= 1 && taskNumber <= tasks.size();
    }
}

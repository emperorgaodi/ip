import java.util.ArrayList;

public class TaskList {
    private static final int MAX_TASKS = 100;
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) throws DarwinException {
        if (tasks.size() >= MAX_TASKS) {
            throw new DarwinException(" Task list is full! Cannot add more tasks.");
        }
        tasks.add(task);
    }

    public Task deleteTask(int taskNumber) throws DarwinException {
        if (!isValidTaskNumber(taskNumber)) {
            throw new DarwinException(" Task list is full! Cannot add more tasks.");
        }
        return tasks.remove(taskNumber - 1);
    }

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

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public int getTaskCount() {
        return tasks.size();
    }

    public Task getTask(int taskNumber) throws DarwinException {
        if (!isValidTaskNumber(taskNumber)) {
            throw new DarwinException("Invalid task number.");
        }
        return tasks.get(taskNumber - 1);
    }

    public boolean isValidTaskNumber(int taskNumber) {
        return taskNumber >= 1 && taskNumber <= tasks.size();
    }
}

package darwin.command;

import darwin.DarwinException;
import darwin.storage.Storage;
import darwin.task.Task;
import darwin.task.TaskList;
import darwin.task.ToDo;
import darwin.ui.Ui;

/**
 * Represents a command to add a new todo task. A <code>TodoCommand</code> object
 * creates a simple task without any date or time constraints.
 */
public class TodoCommand extends Command {

    private final String description;

    /**
     * Constructs a TodoCommand with the specified task description.
     *
     * @param description The text description of the todo task to be created.
     */
    public TodoCommand(String description) {
        this.description = description;
    }

    /**
     * Executes the todo command by creating a new ToDo task and adding it to the task list.
     * Updates the user interface to confirm the task addition and shows the updated task count.
     *
     * @param tasks The TaskList to which the new todo task will be added.
     * @param ui The Ui component for displaying the addition confirmation message.
     * @param storage The Storage component for saving the updated task list to file.
     * @throws DarwinException If the task cannot be added, for example when the task list
     *         has reached its maximum capacity.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DarwinException {
        Task task = new ToDo(description);
        tasks.addTask(task);
        ui.printTaskAdded(task, tasks.getTaskCount());
    }
}

package darwin.command;

import darwin.DarwinException;
import darwin.storage.Storage;
import darwin.task.Task;
import darwin.task.TaskList;
import darwin.task.ToDo;
import darwin.ui.Ui;

public class TodoCommand extends Command {
    private final String description;

    public TodoCommand(String description) {
        this.description = description;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DarwinException {
        Task task = new ToDo(description);
        tasks.addTask(task);
        ui.printTaskAdded(task, tasks.getTaskCount());
    }
}

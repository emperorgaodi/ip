package darwin.command;

import darwin.DarwinException;
import darwin.storage.Storage;
import darwin.task.Deadline;
import darwin.task.Task;
import darwin.task.TaskList;
import darwin.ui.Ui;

public class DeadlineCommand extends Command {
    private final String description;
    private final String by;

    public DeadlineCommand(String description, String by) {
        this.description = description;
        this.by = by;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DarwinException {
        Task task = new Deadline(description, by);
        tasks.addTask(task);
        ui.printTaskAdded(task, tasks.getTaskCount());
    }
}

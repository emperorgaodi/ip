package darwin.command;

import darwin.DarwinException;
import darwin.task.Task;
import darwin.task.TaskList;
import darwin.ui.Ui;
import darwin.storage.Storage;

public class DeleteCommand extends Command {

    private final int taskNumber;

    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    public void execute(TaskList tasks, Ui ui, Storage storage) throws DarwinException {
        Task removedTask = tasks.deleteTask(taskNumber);
        ui.showDeletedTask(removedTask, tasks.getTaskCount());
    }

}

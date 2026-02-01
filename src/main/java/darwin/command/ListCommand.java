package darwin.command;

import darwin.storage.Storage;
import darwin.task.TaskList;
import darwin.ui.Ui;

public class ListCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.printTaskList(tasks.getTasks());
    }
}

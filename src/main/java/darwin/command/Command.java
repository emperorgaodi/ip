package darwin.command;

import darwin.DarwinException;
import darwin.storage.Storage;
import darwin.task.TaskList;
import darwin.ui.Ui;

public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws DarwinException;
    public boolean isExit() {
        return false;
    }
}

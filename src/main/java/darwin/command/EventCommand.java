package darwin.command;

import darwin.DarwinException;
import darwin.storage.Storage;
import darwin.task.Event;
import darwin.task.Task;
import darwin.task.TaskList;
import darwin.ui.Ui;

public class EventCommand extends Command {

    private final String description;
    private final String from;
    private final String to;

    public EventCommand(String description, String from, String to) {
        this.description = description;
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DarwinException {
        Task task = new Event(description, from, to);
        tasks.addTask(task);
        ui.printTaskAdded(task, tasks.getTaskCount());
    }
}

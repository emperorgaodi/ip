package darwin.command;

import darwin.DarwinException;
import darwin.DateParser;
import darwin.storage.Storage;
import darwin.task.Event;
import darwin.task.Task;
import darwin.task.TaskList;
import darwin.ui.Ui;

import java.time.LocalDate;

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
        validateDates();

        Task task = new Event(description, from, to);
        tasks.addTask(task);
        ui.printTaskAdded(task, tasks.getTaskCount());
    }

    private void validateDates() throws DarwinException {
        LocalDate fromDate = DateParser.parseDate(from);
        LocalDate toDate = DateParser.parseDate(to);

        if (fromDate.isAfter(toDate)) {
            String fromDisplay = DateParser.formatDateForDisplay(fromDate);
            String toDisplay = DateParser.formatDateForDisplay(toDate);
            throw new DarwinException(
                    " Invalid event dates: Start date (" + fromDisplay +
                            ") cannot be after end date (" + toDisplay + ")."
            );
        }
    }
}

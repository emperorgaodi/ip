package darwin.command;

import darwin.DarwinException;
import darwin.storage.Storage;
import darwin.task.Task;
import darwin.task.TaskList;
import darwin.ui.Ui;
import java.util.ArrayList;

/**
 * Represents a command to search for tasks containing a specific keyword.
 * A <code>FindCommand</code> object searches through all tasks and displays
 * those whose descriptions contain the given search term.
 */
public class FindCommand extends Command {

    private final String keyword;

    /**
     * Constructs a FindCommand with the specified search keyword.
     *
     * @param keyword The search term to look for in task descriptions.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Executes the find command by searching for tasks containing the keyword
     * and displaying the matching results.
     *
     * @param tasks The TaskList to search through.
     * @param ui The Ui for displaying the search results.
     * @param storage The Storage (not used in this command).
     * @throws DarwinException If the keyword is empty.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DarwinException {
        if (keyword.isEmpty()) {
            throw new DarwinException("Please provide a keyword to search for.");
        }

        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        ui.printFoundTasks(matchingTasks, keyword);
    }
}

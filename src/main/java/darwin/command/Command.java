package darwin.command;

import darwin.DarwinException;
import darwin.storage.Storage;
import darwin.task.TaskList;
import darwin.ui.Ui;

/**
 * Represents an abstract command in the Darwin application. A <code>Command</code> object
 * encapsulates a user action that can be executed on the task list, with access to
 * the user interface and storage systems.
 */
public abstract class Command {

    /**
     * Executes the specific command using the provided application components.
     * The exact behavior depends on the concrete command implementation.
     *
     * @param tasks The TaskList containing the current tasks to operate on.
     * @param ui The Ui component for displaying output and interacting with the user.
     * @param storage The Storage component for persisting task changes to file.
     * @throws DarwinException If an error occurs during command execution, such as
     *         invalid task numbers or missing parameters.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws DarwinException;

    /**
     * Determines whether this command should terminate the application.
     * Most commands return false; only the exit command returns true.
     *
     * @return true if this command is an exit command that should end the application,
     *         false otherwise.
     */
    public boolean isExit() {
        return false;
    }
}

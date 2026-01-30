public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws DarwinException;
    public boolean isExit() {
        return false;
    }
}

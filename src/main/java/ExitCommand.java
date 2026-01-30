public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        // nothing to do for exit
    }

    @Override
    public boolean isExit() {
        return true;
    }
}

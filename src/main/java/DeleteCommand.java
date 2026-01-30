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

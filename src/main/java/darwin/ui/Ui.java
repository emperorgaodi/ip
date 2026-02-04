package darwin.ui;

import darwin.task.Task;

import java.util.ArrayList;
import java.util.Scanner; // not used for GUI, kept for legacy usage

public class Ui {

    private static final String HORIZONTAL_LINE = "____________________________________________________________";
    private final Scanner scanner; // not used for GUI, kept for legacy usage

    private StringBuilder responseBuilder; // for GUI mode

    public Ui() {
        this.scanner = new Scanner(System.in); // not used for GUI, kept for legacy usage
        this.responseBuilder = new StringBuilder();
    }

    public void setResponseBuilder(StringBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    public void clearResponse() {
        if (responseBuilder != null) {
            responseBuilder.setLength(0);
        }
    }

    public void printGreeting() {
        String greeting = "Hello! I'm Darwin\nWhat can I do for you?";
        output(greeting);
    }

    public void printGoodbye() {
        String byeMessage = "Bye. Hope to see you again soon!";
        output(byeMessage);
    }

    public void printLine() {
        output(HORIZONTAL_LINE);
    }

    public String readCommand() { // not used for GUI, kept for legacy usage
        return scanner.nextLine().trim();
    }

    public void printError(String errorMessage) {
        output(errorMessage);
    }

    public void printTaskAdded(Task task, int taskCount) {
        String taskAddedMessage = "Got it. I've added this task:\n" +
                " " + task + "\n" +
                "Now you have " + taskCount + " tasks in the list.";
        output(taskAddedMessage);
    }

    public void printTaskList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            output("No tasks in your list.");
        } else {
            output("Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                output(" " + (i + 1) + "." + tasks.get(i));
            }
        }
    }

    public void printMarkedTask(Task task, boolean isDone) {
        if (isDone) {
            output("Nice! I've marked this task as done:");
        } else {
            output("OK, I've marked this task as not done yet:");
        }
        output("  " + task);
    }

    public void printFoundTasks(ArrayList<Task> foundTasks, String keyword) {
        if (foundTasks.isEmpty()) {
            output("No tasks found containing: \"" + keyword + "\"");
        } else {
            output("Here are the matching tasks in your list:");
            for (int i = 0; i < foundTasks.size(); i++) {
                output(" " + (i + 1) + "." + foundTasks.get(i));
            }
        }
    }

    public void showDeletedTask(Task task, int taskCount) {
        String taskDeletedMessage = "Noted. I've removed this task:\n" +
                " " + task + "\n" +
                "Now you have " + taskCount + " tasks in the list.";
        output(taskDeletedMessage);
    }

    private void output(String text) {
        if (responseBuilder != null) {
            responseBuilder.append(text).append("\n");
        }
    }

    public void close() { // not used for GUI, kept for legacy usage
        scanner.close();
    }
}

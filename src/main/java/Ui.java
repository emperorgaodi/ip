import java.util.ArrayList;
import java.util.Scanner;

public class Ui {
    private static final String HORIZONTAL_LINE = "____________________________________________________________";
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void printGreeting() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(" Hello! I'm Darwin");
        System.out.println(" What can I do for you?");
        System.out.println(HORIZONTAL_LINE);
    }

    public void printGoodbye() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(HORIZONTAL_LINE);
    }

    public void printLine() {
        System.out.println(HORIZONTAL_LINE);
    }

    public String readCommand() {
        return scanner.nextLine().trim();
    }

    public void printError(String errorMessage) {
        System.out.println("Error: " + errorMessage);
    }

    public void printTaskAdded(Task task, int taskCount) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    public void printTaskList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println(" No tasks in your list.");
        } else {
            System.out.println(" Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(" " + (i + 1) + "." + tasks.get(i));
            }
        }
    }

    public void printMarkedTask(Task task, boolean isDone) {
        if (isDone) {
            System.out.println(" Nice! I've marked this task as done:");
        } else {
            System.out.println(" OK, I've marked this task as not done yet:");
        }
        System.out.println("   " + task);
    }

    public void showDeletedTask(Task task, int taskCount) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    public void close() {
        scanner.close();
    }

}

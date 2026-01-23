import java.util.Scanner;

public class Darwin {
    private static final int MAX_TASKS = 100;
    private static Task[] tasks = new Task[MAX_TASKS];
    private static int taskCount = 0;

    public static void main(String[] args) {
        runChatBot();
    }

    private static void runChatBot() {
        Scanner scanner = new Scanner(System.in);

        showGreeting();

        while (true) {
            String input = getUserInput(scanner);

            if (isExitCommand(input)) {
                showGoodbye();
                break;
            }

            processCommand(input);
        }

        scanner.close();
    }

    private static void showGreeting() {
        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Darwin");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");
    }

    private static String getUserInput(Scanner scanner) {
        return scanner.nextLine().trim();
    }

    private static boolean isExitCommand(String input) {
        return input.equalsIgnoreCase("bye");
    }

    private static void processCommand(String input) {
        System.out.println("____________________________________________________________");

        if (input.equalsIgnoreCase("list")) {
            showTasklist();
        } else if (input.startsWith("mark ")) {
            markTask(input);
        } else if (input.startsWith("unmark ")) {
            unmarkTask(input);
        } else {
            addTask(input);
        }

        System.out.println("____________________________________________________________");
    }

    private static void showTasklist() {
        if (taskCount == 0) {
            System.out.println("No tasks in your list.");
        } else {
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < taskCount; i++) {
                System.out.println(" " + (i + 1) + "." + tasks[i]);
            }
        }
    }

    private static void addTask(String taskDescription) {
        if (taskCount < MAX_TASKS) {
            tasks[taskCount] = new Task(taskDescription);
            taskCount++;
            System.out.println("added: " + taskDescription);
        } else {
            // Assume there will be no more than 100 tasks.
        }
    }

    private static void markTask(String input) {
        try {
            int taskNumber = Integer.parseInt(input.substring(5).trim());
            if (isValidTaskNumber(taskNumber)) {
                tasks[taskNumber - 1].markAsDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("   " + tasks[taskNumber - 1]);
            } else {
                System.out.println(" Invalid task number.");
            }
        } catch (NumberFormatException e) {
            System.out.println(" Please provide a valid task number after 'mark'.");
        }
    }

    private static void unmarkTask(String input) {
        try {
            int taskNumber = Integer.parseInt(input.substring(7).trim());
            if (isValidTaskNumber(taskNumber)) {
                tasks[taskNumber - 1].markAsNotDone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("   " + tasks[taskNumber - 1]);
            } else {
                System.out.println(" Invalid task number.");
            }
        } catch (NumberFormatException e) {
            System.out.println(" Please provide a valid task number after 'unmark'.");
        }
    }

    private static boolean isValidTaskNumber(int taskNumber) {
        return taskNumber >= 1 && taskNumber <= taskCount;
    }

    private static void showGoodbye() {
        System.out.println("____________________________________________________________");
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");
    }
}

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
        } else if (input.startsWith("todo ")) {
            addTodo(input);
        } else if (input.startsWith("deadline ")) {
            addDeadline(input);
        } else if (input.startsWith("event ")) {
            addEvent(input);
        } else {
            System.out.println(" Unknown command. Please use: todo, deadline, event, list, mark, unmark, or bye");
        }

        System.out.println("____________________________________________________________");
    }

    private static void showTasklist() {
        if (taskCount == 0) {
            System.out.println(" No tasks in your list.");
        } else {
            System.out.println(" Here are the tasks in your list:");
            for (int i = 0; i < taskCount; i++) {
                System.out.println(" " + (i + 1) + "." + tasks[i]);
            }
        }
    }

    private static void addTodo(String input) {
        if (input.length() <= 5) {
            System.out.println(" OOPS!!! The description of a todo cannot be empty.");
            return;
        }

        String description = input.substring(5).trim();
        if (description.isEmpty()) {
            System.out.println(" OOPS!!! The description of a todo cannot be empty.");
            return;
        }

        if (taskCount < MAX_TASKS) {
            tasks[taskCount] = new ToDo(description);
            taskCount++;
            printTaskAddedMessage(tasks[taskCount - 1]);
        } else {
            System.out.println(" Task list is full! Cannot add more tasks.");
        }
    }

    private static void addDeadline(String input) {
        if (input.length() <= 9) {
            System.out.println(" OOPS!!! The description of a deadline cannot be empty.");
            return;
        }

        String remaining = input.substring(9).trim();
        String[] parts = remaining.split(" /by ", 2);

        if (parts.length < 2) {
            System.out.println(" OOPS!!! Please use the format: deadline <description> /by <time>");
            return;
        }

        String description = parts[0].trim();
        String by = parts[1].trim();

        if (description.isEmpty() || by.isEmpty()) {
            System.out.println(" OOPS!!! The description and deadline time cannot be empty.");
            return;
        }

        if (taskCount < MAX_TASKS) {
            tasks[taskCount] = new Deadline(description, by);
            taskCount++;
            printTaskAddedMessage(tasks[taskCount - 1]);
        } else {
            System.out.println(" Task list is full! Cannot add more tasks.");
        }
    }

    private static void addEvent(String input) {
        if (input.length() <= 6) {
            System.out.println(" OOPS!!! The description of a event cannot be empty.");
            return;
        }

        String remaining = input.substring(6).trim();
        String[] parts = remaining.split(" /from ", 2);

        if (parts.length < 2) {
            System.out.println(" OOPS!!! Please use the format: event <description> /from <start> /to <end>");
            return;
        }

        String description = parts[0].trim();
        String[] timeParts = parts[1].split(" /to ", 2);

        if (timeParts.length < 2) {
            System.out.println(" OOPS!!! Please use the format: event <description> /from <start> /to <end>");
            return;
        }

        String from = timeParts[0].trim();
        String to = timeParts[1].trim();

        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            System.out.println(" OOPS!!! The description, start time, and end time cannot be empty.");
            return;
        }

        if (taskCount < MAX_TASKS) {
            tasks[taskCount] = new Event(description, from, to);
            taskCount++;
            printTaskAddedMessage(tasks[taskCount - 1]);
        } else {
            System.out.println(" Task list is full! Cannot add more tasks.");
        }
    }

    private static void printTaskAddedMessage(Task task) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    private static void markTask(String input) {
        try {
            String[] parts = input.split("\\s+");
            if (parts.length >= 2) {
                int taskNumber = Integer.parseInt(parts[1]);
                if(isValidTaskNumber(taskNumber)) {
                    tasks[taskNumber - 1].markAsDone();
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + tasks[taskNumber - 1]);
                } else {
                    // checks for valid task number - failed case
                    System.out.println(" Invalid task number.");
                }
            } else {
                // less than 2 parts in "parts"
                System.out.println(" Please specify a task number.");
            }
        } catch (NumberFormatException e) {
            System.out.println(" Please provide a valid task number after 'mark'.");
        }
    }

    private static void unmarkTask(String input) {
        try {
            String[] parts = input.split("\\s+");
            if (parts.length >= 2) {
                int taskNumber = Integer.parseInt(parts[1]);
                if(isValidTaskNumber(taskNumber)) {
                    tasks[taskNumber - 1].markAsNotDone();
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + tasks[taskNumber - 1]);
                } else {
                    // checks for valid task number - failed case
                    System.out.println(" Invalid task number.");
                }
            } else {
                // less than 2 parts in "parts"
                System.out.println(" Please specify a task number.");
            }
        } catch (NumberFormatException e) {
            System.out.println(" Please provide a valid task number after 'mark'.");
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

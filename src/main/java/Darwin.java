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

            try{
                processCommand(input);
            } catch (DarwinException e) {
                System.out.println("Error: " + e.getMessage());
            }
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

    private static void processCommand(String input) throws DarwinException {
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
            throw new DarwinException(" Unknown command :( Please use: todo, deadline, event, list, mark, unmark, " +
                    "or bye");
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

    private static void addTodo(String input) throws DarwinException {
        if (input.length() <= 5) {
            throw new DarwinException(" Please use this format 'todo <description>'!");
        }

        String description = input.substring(5).trim();
        if (description.isEmpty()) {
            throw new DarwinException(" Description is empty! Please use this format 'todo <description>'!");
        }

        if (taskCount >= MAX_TASKS) {
            throw new DarwinException(" Task list is full! Cannot add more tasks.");
        }

        tasks[taskCount] = new ToDo(description);
        taskCount++;
        printTaskAddedMessage(tasks[taskCount - 1]);
    }

    private static void addDeadline(String input) throws DarwinException {
        if (input.length() <= 9) {
            throw new DarwinException(" Please use this format 'deadline <description> /by <deadline>'!");
        }

        String remaining = input.substring(9).trim();
        String[] parts = remaining.split(" /by ", 2);

        if (parts.length < 2) {
            throw new DarwinException(" Please use this format 'deadline <description> /by <deadline>'!");
        }

        String description = parts[0].trim();
        String by = parts[1].trim();

        if (description.isEmpty() || by.isEmpty()) {
            throw new DarwinException(" Description and/or deadline is empty. Please use this format 'deadline " +
                    "<description> /by <deadline>'!");
        }

        if (taskCount >= MAX_TASKS) {
            throw new DarwinException(" Task list is full! Cannot add more tasks.");
        }

        tasks[taskCount] = new Deadline(description, by);
        taskCount++;
        printTaskAddedMessage(tasks[taskCount - 1]);
    }

    private static void addEvent(String input) throws DarwinException {
        if (input.length() <= 6) {
            throw new DarwinException(" Please use this format: event <description> /from <start> /to <end>!");
        }

        String remaining = input.substring(6).trim();
        String[] parts = remaining.split(" /from ", 2);

        if (parts.length < 2) {
            throw new DarwinException(" Please use this format: event <description> /from <start> /to <end>!");
        }

        String description = parts[0].trim();
        String[] timeParts = parts[1].split(" /to ", 2);

        if (timeParts.length < 2) {
            throw new DarwinException(" Please use this format: event <description> /from <start> /to <end>!");
        }

        String from = timeParts[0].trim();
        String to = timeParts[1].trim();

        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new DarwinException(" The description, start time, and/or end time is empty. Please use this " +
                    "format: event <description> /from <start> /to <end>!");
        }

        if (taskCount >= MAX_TASKS) {
            throw new DarwinException(" Task list is full! Cannot add more tasks.");
        }

        tasks[taskCount] = new Event(description, from, to);
        taskCount++;
        printTaskAddedMessage(tasks[taskCount - 1]);
    }

    private static void printTaskAddedMessage(Task task) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    private static void markTask(String input) throws DarwinException {
        try {
            String[] parts = input.split("\\s+");

            if (parts.length < 2) {
                throw new DarwinException(" Please specify a task number.");
            }

            int taskNumber = Integer.parseInt(parts[1]);

            if(!isValidTaskNumber(taskNumber)) {
                throw new DarwinException(" Invalid task number.");
            }

            tasks[taskNumber - 1].markAsDone();
            System.out.println(" Nice! I've marked this task as done:");
            System.out.println("   " + tasks[taskNumber - 1]);

        } catch (NumberFormatException e) {
            throw new DarwinException(" Please provide a valid task number after 'mark'.");
        }
    }

    private static void unmarkTask(String input) throws DarwinException {
        try {
            String[] parts = input.split("\\s+");
            if (parts.length < 2) {
                throw new DarwinException(" Please specify a task number.");
            }

            int taskNumber = Integer.parseInt(parts[1]);

            if (!isValidTaskNumber(taskNumber)) {
                throw new DarwinException(" Invalid task number.");
            }

            tasks[taskNumber - 1].markAsNotDone();
            System.out.println(" OK, I've marked this task as not done yet:");
            System.out.println("   " + tasks[taskNumber - 1]);

        } catch (NumberFormatException e) {
            throw new DarwinException(" Please provide a valid task number after 'unmark'.");
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

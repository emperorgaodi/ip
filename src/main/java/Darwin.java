import java.awt.*;
import java.awt.font.NumericShaper;
import java.util.Scanner;
import java.util.ArrayList;

public class Darwin {
    private static final int MAX_TASKS = 100;
    private static ArrayList<Task> tasks = new ArrayList<>();
    private static Storage storage;

    private static final String horizontal_line = "____________________________________________________________";
    private static final String FILE_PATH = "./data/darwin.txt";

    public static void main(String[] args) {
        // initialise storage and load tasks
        storage = new Storage(FILE_PATH);
        tasks = storage.loadTasks(); // load tasks from file

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
                storage.saveTasks(tasks);
            } catch (DarwinException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void showGreeting() {
        System.out.println(horizontal_line);
        System.out.println(" Hello! I'm Darwin");
        System.out.println(" What can I do for you?");
        System.out.println(horizontal_line);
    }

    private static String getUserInput(Scanner scanner) {
        return scanner.nextLine().trim();
    }

    private static boolean isExitCommand(String input) {
        return input.equalsIgnoreCase("bye");
    }

    private static void processCommand(String input) throws DarwinException {
        System.out.println(horizontal_line);

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
        } else if (input.startsWith("delete ")) {
            deleteTask(input);
        } else {
            throw new DarwinException(" Unknown command :( Please use: todo, deadline, event, list, mark, unmark, " +
                    "delete or bye");
        }

        System.out.println(horizontal_line);
    }

    private static void showTasklist() {
        if (tasks.isEmpty()) {
            System.out.println(" No tasks in your list.");
        } else {
            System.out.println(" Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(" " + (i + 1) + "." + tasks.get(i));
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

        if (tasks.size() >= MAX_TASKS) {
            throw new DarwinException(" Task list is full! Cannot add more tasks.");
        }

        tasks.add(new ToDo(description));
        printTaskAddedMessage(tasks.get(tasks.size() - 1));
    }

    private static void addDeadline(String input) throws DarwinException {
        if (input.length() <= 9) {
            throw new DarwinException(" Please use this format 'deadline <description> /by yyyy-mm-dd'!");
        }

        String remaining = input.substring(9).trim();
        String[] parts = remaining.split(" /by ", 2);

        if (parts.length < 2) {
            throw new DarwinException(" Please use this format 'deadline <description> /by yyyy-mm-dd'!");
        }

        String description = parts[0].trim();
        String by = parts[1].trim();

        if (description.isEmpty() || by.isEmpty()) {
            throw new DarwinException(" Description and/or deadline is empty. Please use this format 'deadline " +
                    "<description> /by yyyy-mm-dd'!");
        }

        if (tasks.size() >= MAX_TASKS) {
            throw new DarwinException(" Task list is full! Cannot add more tasks.");
        }

        tasks.add(new Deadline(description, by));
        printTaskAddedMessage(tasks.get(tasks.size() - 1));
    }

    private static void addEvent(String input) throws DarwinException {
        if (input.length() <= 6) {
            throw new DarwinException(" Please use this format: event <description> /from yyyy-mm-dd /to yyyy-mm-dd!");
        }

        String remaining = input.substring(6).trim();
        String[] parts = remaining.split(" /from ", 2);

        if (parts.length < 2) {
            throw new DarwinException(" Please use this format: event <description> /from yyyy-mm-dd /to yyyy-mm-dd!");
        }

        String description = parts[0].trim();
        String[] timeParts = parts[1].split(" /to ", 2);

        if (timeParts.length < 2) {
            throw new DarwinException(" Please use this format: event <description> /from yyyy-mm-dd /to yyyy-mm-dd!");
        }

        String from = timeParts[0].trim();
        String to = timeParts[1].trim();

        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new DarwinException(" The description, start time, and/or end time is empty. Please use this " +
                    "format: event <description> /from yyyy-mm-dd /to yyyy-mm-dd!");
        }

        if (tasks.size() >= MAX_TASKS) {
            throw new DarwinException(" Task list is full! Cannot add more tasks.");
        }

        tasks.add(new Event(description, from, to));
        printTaskAddedMessage(tasks.get(tasks.size() - 1));
    }

    private static void printTaskAddedMessage(Task task) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
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

            tasks.get(taskNumber - 1).markAsDone();
            System.out.println(" Nice! I've marked this task as done:");
            System.out.println("   " + tasks.get(taskNumber - 1));

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

            tasks.get(taskNumber - 1).markAsNotDone();
            System.out.println(" OK, I've marked this task as not done yet:");
            System.out.println("   " + tasks.get(taskNumber - 1));

        } catch (NumberFormatException e) {
            throw new DarwinException(" Please provide a valid task number after 'unmark'.");
        }
    }

    private static void deleteTask(String input) throws DarwinException {
        try {
            String[] parts = input.split("\\s+");

            if (parts.length < 2) {
                throw new DarwinException(" Please specify a task number.");
            }

            int taskNumber = Integer.parseInt(parts[1]);

            if (!isValidTaskNumber(taskNumber)) {
                throw new DarwinException(" Invalid task number.");
            }

            Task removedTask = tasks.remove(taskNumber - 1);
            System.out.println(" Noted. I've removed this task:");
            System.out.println("   " + removedTask);
            System.out.println(" Now you have " + tasks.size() + " tasks in the list.");

        } catch (NumberFormatException e) {
            throw new DarwinException(" Please provide a valid task number after 'delete'.");
        }
    }

    private static boolean isValidTaskNumber(int taskNumber) {
        return taskNumber >= 1 && taskNumber <= tasks.size();
    }

    private static void showGoodbye() {
        System.out.println(horizontal_line);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(horizontal_line);
    }
}

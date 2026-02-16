### Increment: A-AiAssisted, A-Personality

Prompt: Edit Ui.java to make Darwin's character more unique

Response:
I'll help you edit the phrases used by Darwin to give it a unique personality.
Looking at your code, the phrases are primarily in the Ui.java file.
Here's an updated version with a Professor Darwin personality -
a knowledgeable but slightly eccentric academic:
```java
package darwin.ui;

import darwin.task.Task;

import java.util.ArrayList;

public class Ui {

    private static final String HORIZONTAL_LINE = "____________________________________________________________";

    private StringBuilder responseBuilder; // for GUI mode

    public Ui() {
        this.responseBuilder = new StringBuilder();
    }

    public void setResponseBuilder(StringBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    public StringBuilder getResponseBuilder() {
        return responseBuilder;
    }

    public void clearResponse() {
        if (responseBuilder != null) {
            responseBuilder.setLength(0);
        }
    }

    public void printGreeting() {
        String greeting = "Ah, good to see you! Professor Darwin at your service.\n" +
                          "What academic pursuit shall we undertake today?";
        output(greeting);
    }

    public void printGoodbye() {
        String byeMessage = "Farewell, my scholarly friend! Until our next intellectual discourse.\n" +
                            "Remember: knowledge is a journey, not a destination!";
        output(byeMessage);
    }

    public void printLine() {
        output(HORIZONTAL_LINE);
    }

    public void printError(String errorMessage) {
        output("Hmm, that doesn't quite compute... " + errorMessage);
    }

    public void printTaskAdded(Task task, int taskCount) {
        String taskAddedMessage = "Excellent observation! I've documented this task:\n" +
                " " + task + "\n" +
                "Your scholarly collection now holds " + taskCount + " fascinating tasks.";
        output(taskAddedMessage);
    }

    public void printTaskList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            output("Your research archive appears to be empty. How peculiar!");
        } else {
            output("Behold! The complete catalog of your academic endeavors:");
            for (int i = 0; i < tasks.size(); i++) {
                output(" " + (i + 1) + "." + tasks.get(i));
            }
        }
    }

    public void printMarkedTask(Task task, boolean isDone) {
        if (isDone) {
            output("Splendid! Another achievement unlocked:");
        } else {
            output("Very well, this task shall remain on the research agenda:");
        }
        output("  " + task);
    }

    public void printFoundTasks(ArrayList<Task> foundTasks, String keyword) {
        if (foundTasks.isEmpty()) {
            output("My extensive search yields no results for: \"" + keyword + "\"");
        } else {
            output("Eureka! I've discovered these relevant findings:");
            for (int i = 0; i < foundTasks.size(); i++) {
                output(" " + (i + 1) + "." + foundTasks.get(i));
            }
        }
    }

    public void showDeletedTask(Task task, int taskCount) {
        String taskDeletedMessage = "A task removed from the archives:\n" +
                " " + task + "\n" +
                "Your collection now contains " + taskCount + " remaining artifacts.";
        output(taskDeletedMessage);
    }

    private void output(String text) {
        if (responseBuilder != null) {
            responseBuilder.append(text).append("\n");
        }
    }
}
```



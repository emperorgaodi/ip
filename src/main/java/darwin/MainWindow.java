package darwin;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Darwin darwin;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image darwinImage = new Image(this.getClass().getResourceAsStream("/images/DaDarwin.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());


    }

    private void showGreeting() {
        String greeting = darwin.getGreeting();
        dialogContainer.getChildren().addAll(
                DialogBox.getDarwinDialog(greeting, darwinImage)
        );
    }

    /** Injects the Darwin instance */
    public void setDarwin(Darwin d) {
        this.darwin = d;
        // Show greeting when window opens, only possible after darwin has been initialised
        showGreeting();
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Darwin's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = darwin.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDarwinDialog(response, darwinImage)
        );
        userInput.clear();

        if (response.contains("Bye. Hope to see you again soon!")) {
            handleExit();
        }

      // autoscroll downwards after the new message bubbles have been added
      Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }

    private void handleExit() {
        // Close the application after a short delay
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> Platform.exit());
        delay.play();
    }
}
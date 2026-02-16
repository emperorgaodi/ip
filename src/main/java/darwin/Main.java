package darwin;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Darwin using FXML.
 */
public class Main extends Application {

    private static final String DEFAULT_FILE_PATH = "./data/darwin.txt";
    private Darwin darwin = new Darwin(DEFAULT_FILE_PATH);

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);

            stage.setTitle("Professor Darwin's Task Manager");

            stage.setScene(scene);
            stage.setMinHeight(220);
            stage.setMinWidth(500);

            fxmlLoader.<MainWindow>getController().setDarwin(darwin);  // inject the Darwin instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
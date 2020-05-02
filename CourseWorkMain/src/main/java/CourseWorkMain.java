import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class CourseWorkMain{
    public static class MainLaunch extends Application {
        public static Parent root;
        public static Node processScene;
        public static Node userInputScene;
        public static Node resultsScene;
        public static Node settingsScene;

        static {
            try {
                processScene = FXMLLoader.load(MainLaunch.class.getResource("/processScene.fxml"));
                userInputScene = FXMLLoader.load(MainLaunch.class.getResource("/userInputScene.fxml"));
                resultsScene = FXMLLoader.load(MainLaunch.class.getResource("/resultsScene.fxml"));
                settingsScene = FXMLLoader.load(MainLaunch.class.getResource("/settingsScene.fxml"));
            }
            catch (IOException ex){

            }
        }

        @Override
        public void start(Stage primaryStage) throws Exception {

            root = FXMLLoader.load(getClass().getResource("/main.fxml"));
            primaryStage.setTitle("Connectomes");
            Scene scene = new Scene(root, 1000, 800);

            Button currentMainButton = new Button();

            for (Node n : ((VBox)(((BorderPane)root).getLeft())).getChildren()) {
                if (n.getId().equals("settings")) {
                    currentMainButton = (Button)n;
                }
            }
            currentMainButton.setMaxHeight(Double.MAX_VALUE);

            VBox.setVgrow(currentMainButton, Priority.ALWAYS);

            primaryStage.setScene(scene);
            primaryStage.show();
        }

        public static void openUserInput() {
//            userInputScene = FXMLLoader.load(MainLaunch.class.getResource("/userInputScene.fxml"));
            ((BorderPane)root).setCenter(userInputScene);
        }

        public static void openProcess() {
//            processScene = FXMLLoader.load(MainLaunch.class.getResource("/processScene.fxml"));
            ((BorderPane)root).setCenter(processScene);
        }

        public static void openResults() {
//            resultsScene = FXMLLoader.load(MainLaunch.class.getResource("/resultsScene.fxml"));
            ((BorderPane)root).setCenter(resultsScene);
        }

        public static void openSettings() {
//            settingsScene = FXMLLoader.load(MainLaunch.class.getResource("/settingsScene.fxml"));
            ((BorderPane)root).setCenter(settingsScene);
        }
    }

    public static void main(String[] args) {
        Application.launch(MainLaunch.class);

        System.out.println("Hello world ");
    }

}

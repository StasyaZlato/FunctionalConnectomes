package main;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import pojo.AppData;
import pojo.TDAResponse;
import processing.ProcessFile;
import sun.rmi.rmic.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CourseWorkMain{
    public static class MainLaunch extends Application {
        public static Parent root;
        public static Node processScene;
        public static Node userInputScene;
        public static Node oneFileResultsScene;
        public static Node fewFilesResultsScene;
        public static Node settingsScene;

        public static AppData data;

        private static Stage primaryStage;

        public static Stage getPrimaryStage() {
            return primaryStage;
        }

        private static void setPrimaryStage(Stage pStage) {
            MainLaunch.primaryStage = pStage;
        }

        static {
            try {
                processScene = FXMLLoader.load(MainLaunch.class.getResource("/processScene.fxml"));
                userInputScene = FXMLLoader.load(MainLaunch.class.getResource("/userInputScene.fxml"));
                oneFileResultsScene = FXMLLoader.load(MainLaunch.class.getResource("/oneFileResultsScene.fxml"));
                fewFilesResultsScene = FXMLLoader.load(MainLaunch.class.getResource("/fewFileResultsScene.fxml"));
                settingsScene = FXMLLoader.load(MainLaunch.class.getResource("/settingsScene.fxml"));
            }
            catch (IOException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Произошла ошибка во время загрузки сцены! Возможно, архив " +
                        "приложения был поврежден.");
                alert.show();
            }

            data = new AppData();
        }

        @Override
        public void start(Stage primaryStage) throws Exception {

            setPrimaryStage(primaryStage);

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
            ((BorderPane)root).setCenter(userInputScene);

            TextFlow currentSettings = null;

            for (Node n : ((VBox)(((BorderPane)root).getCenter())).getChildren()) {
                if (n.getId() != null && n.getId().equals("inputInfoVBox")) {
                    for (Node node : (((VBox)n).getChildren())) {
                        if (node.getId() != null && node.getId().equals("currentSettings")) {
                            currentSettings = (TextFlow) node;
                        }
                    }
                }
            }

            if (currentSettings != null) {
                currentSettings.getChildren().clear();
                Text[] ordinary = new Text[] {
                        new Text("Текущие настройки приложения: тип симплициального комплекса "),
                        new Text(", максимальная величина фильтрации "), new Text(", максимальная размерность ")
                };

                Text[] emphasized = new Text[] {
                        new Text(data.getComplexType()),
                        new Text(String.valueOf(data.getMaxFiltrationValue())),
                        new Text(String.valueOf(data.getMaxDimensions()))
                };


                for (Text t : emphasized) {
                    t.getStyleClass().add("text-blue-small");
                }

                ArrayList<Text> flow = new ArrayList<>();

                for (int i = 0; i < 3; i++) {
                    ordinary[i].getStyleClass().add("text-ordinary-small");
                    emphasized[i].getStyleClass().add("text-blue-small");
                    flow.add(ordinary[i]);
                    flow.add(emphasized[i]);
                }

                currentSettings.getChildren().addAll(flow);
            }
        }

        public static void openProcess() {
            ((BorderPane)root).setCenter(processScene);
        }

        public static void openSettings() {
            ((BorderPane)root).setCenter(settingsScene);
        }

        public static void openResults() {
//            if (data.isOneFileAction()) {
            openResultsOneFile();
//            }
        }

        public static void openResultsOneFile() {
            ((BorderPane)root).setCenter(oneFileResultsScene);


            ProgressBar progressBar = (ProgressBar)((VBox)oneFileResultsScene).getChildren().get(1);
            progressBar.progressProperty().unbind();

            Label infoLabel = ((Label)((VBox)oneFileResultsScene).getChildren().get(0));

            if (!data.noFilePathGiven()) {
                if (data.getResults().getTdaResponse().isEmpty() ||
                        !data.getFilePath().equals(data.getResults().getTdaResponse().get(0).getPath())) {
                    infoLabel.setText(String.format("Подождите, пока заершится анализ файла %s", data.getFilePath()));
                    ProcessFile processFile = new ProcessFile(data.getFilePath());
                    progressBar.setVisible(true);
                    Task<TDAResponse> task = new Task<TDAResponse>() {
                        @Override
                        public TDAResponse call() {
                            TDAResponse res = processFile.process2DArray();
                            return res;
                        }
                    };

                    task.setOnSucceeded(e -> {
                        TDAResponse result = task.getValue();
                        data.setResults(result);
                        progressBar.setVisible(false);
                        infoLabel.setText(String.format("Обработка файла %s завершена", data.getFilePath()));
                    });

                    progressBar.progressProperty().bind(task.progressProperty());


                    new Thread(task).start();
                }
            }
            else {
                infoLabel.setText("Файл для обработки не выбран");
                progressBar.setVisible(false);
            }
        }
    }

    public static void main(String[] args) {
        Application.launch(MainLaunch.class);

        System.out.println("Hello world ");
    }

}

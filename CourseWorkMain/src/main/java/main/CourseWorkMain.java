package main;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import pojo.AppData;
import pojo.Interval;
import pojo.TDAResponse;
import processing.ProcessFile;
import sun.rmi.rmic.Main;

import java.io.IOException;
import java.util.*;

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


            ProgressBar progressBar = (ProgressBar)((VBox)(((BorderPane)oneFileResultsScene).getTop())).getChildren().get(1);
            progressBar.progressProperty().unbind();

            if (!data.noFilePathGiven()) {
                if (data.getResults().getTdaResponse().isEmpty() ||
                        !data.getFilePath().equals(data.getResults().getTdaResponse().get(0).getPath())) {

                    ProcessFile processFile = new ProcessFile(data.getFilePath(), data.getComplexType(), data.getMaxDimensions(), data.getMaxFiltrationValue());
                    progressBar.setVisible(true);
                    showAnalysisIsGoingOn();
                    Task<TDAResponse> task = new Task<TDAResponse>() {
                        @Override
                        public TDAResponse call() {
                            return processFile.process2DArray();
                        }
                    };

                    task.setOnSucceeded(e -> {
                        TDAResponse result = task.getValue();
                        data.setResults(result);
                        showAnalysisResults();
                    });

                    progressBar.progressProperty().bind(task.progressProperty());


                    new Thread(task).start();
                }
            }
            else {
                showNoFileResults();
            }
        }

        private static void showNoFileResults() {
            ProgressBar progressBar = (ProgressBar)((VBox)(((BorderPane)oneFileResultsScene).getTop())).getChildren().get(1);
            TextFlow infoLabel = (TextFlow) ((VBox)(((BorderPane)oneFileResultsScene).getTop())).getChildren().get(0);

            Text t1 = new Text("Файл для обработки не выбран.");
            t1.getStyleClass().add("text-ordinary");
            infoLabel.getChildren().clear();
            infoLabel.getChildren().add(t1);
            progressBar.setVisible(false);
        }

        private static void showAnalysisResults() {
            ProgressBar progressBar = (ProgressBar)((VBox)(((BorderPane)oneFileResultsScene).getTop())).getChildren().get(1);
            TextFlow infoLabel = (TextFlow)((VBox)(((BorderPane)oneFileResultsScene).getTop())).getChildren().get(0);

            HBox topInfoLabel = (HBox)((VBox)(((BorderPane)oneFileResultsScene).getTop())).getChildren().get(2);

            HBox bottomBtns = (HBox) (((BorderPane)oneFileResultsScene).getBottom());
            bottomBtns.setVisible(true);

            topInfoLabel.setVisible(true);

            progressBar.setVisible(false);

            Text t11 = new Text("Обработка файла ");
            t11.getStyleClass().add("text-ordinary");
            Text t12 = new Text(data.getFilePath());
            t12.getStyleClass().add("text-blue-small");
            Text t13 = new Text(" завершена");
            t13.getStyleClass().add("text-ordinary");

            infoLabel.getChildren().clear();
            infoLabel.getChildren().addAll(t11, t12, t13);

            Label betti = (Label)topInfoLabel.getChildren().get(0);
            betti.setText(String.format("Числа Бетти: %s", data.getResults().getOnlyTDAResponse().getBettiNumbers()));

           plotBarCode();




        }

        private static void plotBarCode() {
            ScrollPane graphsScroll = (ScrollPane)((BorderPane)oneFileResultsScene).getCenter();

            VBox graphsPanel = (VBox)graphsScroll.getContent();
            graphsPanel.getChildren().clear();
            Set<Map.Entry<Integer, List<Interval>>> entrySet = data.getResults().getOnlyTDAResponse().getIntervals().entrySet();

            for (Map.Entry<Integer, List<Interval>> entry : entrySet) {
                NumberAxis xAxis = new NumberAxis();
                NumberAxis yAxis = new NumberAxis();

                yAxis.setAutoRanging(false);
                yAxis.setLowerBound(0);
                yAxis.setUpperBound(entry.getValue().size() + 1);
                yAxis.setTickUnit(1);
                yAxis.setMinorTickCount(0);

                xAxis.setAutoRanging(false);
                xAxis.setLowerBound(0);
                xAxis.setUpperBound(data.getMaxFiltrationValue());
                xAxis.setTickUnit(data.getMaxFiltrationValue() / 20);

                LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

                lineChart.setTitle(String.format("%d-dimension", entry.getKey()));
                lineChart.setPrefHeight((entry.getValue().size() + 1) * 8);

//                double max = 0;
                int i = 1;
                lineChart.setCreateSymbols(false);
                for (Interval in : entry.getValue()) {
                    XYChart.Series<Number, Number> series = new XYChart.Series<>();
                    series.getData().add(new XYChart.Data<>(in.getStart(), i));
                    if (!in.isLeftInfinite()) {
                        XYChart.Data<Number, Number> right = new XYChart.Data<>(in.getEnd(), i);

                        series.getData().add(right);
                    }
                    else {
                        XYChart.Data<Number, Number> inf = new XYChart.Data<>(data.getMaxFiltrationValue(), i);

                        series.getData().add(inf);
                    }
                    i++;
                    lineChart.getData().add(series);
                    lineChart.setLegendVisible(false);
                }
                lineChart.getYAxis().setTickLabelsVisible(false);
                graphsPanel.getChildren().add(lineChart);
            }
            graphsScroll.setContent(graphsPanel);
            ((BorderPane)oneFileResultsScene).setCenter(graphsScroll);


        }

        private static void showAnalysisIsGoingOn() {
            TextFlow infoLabel = (TextFlow) ((VBox)(((BorderPane)oneFileResultsScene).getTop())).getChildren().get(0);
            HBox topInfoLabel = (HBox)((VBox)(((BorderPane)oneFileResultsScene).getTop())).getChildren().get(2);

            topInfoLabel.setVisible(false);

            Text t1 = new Text("Подождите, пока завершится анализ файла ");
            t1.getStyleClass().add("text-ordinary");
            Text t2 = new Text(data.getFilePath());
            t2.getStyleClass().add("text-blue-small");
            infoLabel.getChildren().clear();
            infoLabel.getChildren().addAll(t1, t2);

            ((BorderPane)oneFileResultsScene).setRight(null);
        }
    }



    public static void main(String[] args) {
        Application.launch(MainLaunch.class);

        System.out.println("Hello world ");
    }

}

package main;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pojo.AppData;
import pojo.Interval;
import pojo.TDAOneFileResponse;
import pojo.TDAResponse;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CourseWorkMain {
    public static void main(String[] args) {
        Application.launch(MainLaunch.class);

        System.out.println("Hello world ");
    }

    public static class MainLaunch extends Application {
        public static Parent root;
        public static Node processScene;
        public static Node userInputScene;
        public static Node oneFileResultsScene;
        public static Node fewFilesResultsScene;
        public static Node settingsScene;

        public static AppData data;

        private static Stage primaryStage;

        static {
            try {
                processScene = FXMLLoader.load(MainLaunch.class.getResource("/fxml/processScene.fxml"));
                userInputScene = FXMLLoader.load(MainLaunch.class.getResource("/fxml/userInputScene.fxml"));
                oneFileResultsScene = FXMLLoader.load(MainLaunch.class.getResource("/fxml/oneFileResultsScene.fxml"));
                fewFilesResultsScene = FXMLLoader.load(MainLaunch.class.getResource("/fxml/fewFileResultsScene.fxml"));
                settingsScene = FXMLLoader.load(MainLaunch.class.getResource("/fxml/settingsScene.fxml"));
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Произошла ошибка во время загрузки сцены! Возможно, архив " +
                        "приложения был поврежден.");
                alert.show();
            }

            data = new AppData();
        }

        public static Stage getPrimaryStage() {
            return primaryStage;
        }

        private static void setPrimaryStage(Stage pStage) {
            MainLaunch.primaryStage = pStage;
        }

        public static void openUserInput() {
            ((BorderPane) root).setCenter(userInputScene);

            TextFlow currentSettings = null;

            for (Node n : ((VBox) (((BorderPane) root).getCenter())).getChildren()) {
                if (n.getId() != null && n.getId().equals("inputInfoVBox")) {
                    for (Node node : (((VBox) n).getChildren())) {
                        if (node.getId() != null && node.getId().equals("currentSettings")) {
                            currentSettings = (TextFlow) node;
                        }
                    }
                }
            }

            if (currentSettings != null) {
                currentSettings.getChildren().clear();
                Text[] ordinary = new Text[]{
                        new Text("Текущие настройки приложения: тип симплициального комплекса "),
                        new Text(", максимальная величина фильтрации "), new Text(", максимальная размерность ")
                };

                Text[] emphasized = new Text[]{
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
            ((BorderPane) root).setCenter(processScene);
        }

        public static void openSettings() {
            ((BorderPane) root).setCenter(settingsScene);
        }

        public static void openResults() {
            if (data.isOneFileAction() || data.noFilePathGiven()) {
                openResultsOneFile();
            } else {
                openResultsGroupOfFiles();
            }
        }

        public static void openResultsOneFile() {
            ((BorderPane) root).setCenter(oneFileResultsScene);


            ProgressBar progressBar = (ProgressBar) ((VBox) (((BorderPane) oneFileResultsScene).getTop())).getChildren().get(1);
            progressBar.progressProperty().unbind();

            if (!data.noFilePathGiven()) {
                if (data.getResults().getTdaResponse().isEmpty() ||
                        !data.getFilePath().equals(data.getResults().getTdaResponse().get(0).getPath())) {

                    progressBar.setVisible(true);
                    showOneFileAnalysisIsGoingOn();

                    ProcessOneFileTask task = new ProcessOneFileTask();

                    task.setOnSucceeded(e -> {
                        TDAResponse result = task.getValue();
                        data.setResults(result);
                        showOneFileAnalysisResults();
                    });

                    progressBar.progressProperty().bind(task.progressProperty());


                    new Thread(task).start();
                }
            } else {
                showNoFileResults();
            }
        }

        private static void openResultsGroupOfFiles() {
            ((BorderPane) root).setCenter(fewFilesResultsScene);

            ProgressBar progressBar = (ProgressBar) ((VBox) ((fewFilesResultsScene))).getChildren().get(1);
            progressBar.progressProperty().unbind();
            progressBar.setProgress(0);

            if (!data.noFilePathGiven()) {
                if (data.getResults().getTdaResponse().isEmpty() || !data.resultsCorrespondPaths()) {

                    progressBar.setVisible(true);
                    showGroupOfFileAnalysisIsGoingOn();
                    Task<TDAResponse> task = new ProcessGroupOfFilesTask();


                    Text t1 = new Text("Выполняется анализ файла ");
                    t1.getStyleClass().add("text-ordinary");
                    Text t2 = new Text();

                    t2.textProperty().unbind();
                    t2.textProperty().bind(task.messageProperty());

                    t2.getStyleClass().add("text-blue-small");
                    ((TextFlow) ((VBox) (fewFilesResultsScene)).getChildren().get(0)).getChildren().clear();
                    ((TextFlow) ((VBox) (fewFilesResultsScene)).getChildren().get(0)).getChildren().addAll(t1, t2);

                    task.setOnSucceeded(e -> {
                        TDAResponse result = task.getValue();
                        data.setResults(result);
                        showGroupOfFileAnalysisResult();
                        progressBar.setVisible(false);
                    });

                    progressBar.progressProperty().bind(task.progressProperty());


                    new Thread(task).start();
                }
            } else {
                showNoFileResults();
            }
        }

        private static void showGroupOfFileAnalysisResult() {
            ArrayList<TitledPane> panes = new ArrayList<>();

            int i = 0;
            for (TDAOneFileResponse r : data.getResults().getTdaResponse()) {
                TitledPane tp = new TitledPane();
                tp.setText(r.getPath());


                ScrollPane mainFileSP = new ScrollPane();

                try {
                    BorderPane newLoadedPane = FXMLLoader.load(MainLaunch.class.getResource("/fxml/oneFileResultsScene.fxml"));
                    ((VBox) (newLoadedPane.getTop())).getChildren().get(1).setVisible(false);
                    TextFlow infoLabel = (TextFlow) ((VBox) fewFilesResultsScene).getChildren().get(0);

                    newLoadedPane.getBottom().setVisible(true);
                    HBox topInfoLabel = (HBox) ((VBox) ((newLoadedPane).getTop())).getChildren().get(2);

                    showIntervals(i, false, newLoadedPane);

                    HBox bottomBtns = (HBox) (newLoadedPane.getBottom());
                    bottomBtns.setVisible(true);

                    topInfoLabel.setVisible(true);

                    Text t1 = new Text("Обработка файла ");
                    t1.getStyleClass().add("text-ordinary");
                    Text t2 = new Text(r.getPath());
                    t2.getStyleClass().add("text-blue-small");
                    Text t3 = new Text(" завершена");
                    t3.getStyleClass().add("text-ordinary");

                    infoLabel.getChildren().clear();
                    infoLabel.getChildren().addAll(t1, t2, t3);

                    Label betti = (Label) topInfoLabel.getChildren().get(0);
                    betti.setText(String.format("Числа Бетти: %s", r.getBettiNumbers()));

                    newLoadedPane.setCenter(plotBarCode(i));

                    ((Button) (bottomBtns.getChildren().get(0))).setOnAction(event -> {
                        saveImg(false, newLoadedPane);
                    });

                    i++;

                    mainFileSP.setContent(newLoadedPane);
                    tp.setContent(mainFileSP);

                    panes.add(tp);
                } catch (IOException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Произошла ошибка во время загрузки сцены! Возможно, архив " +
                            "приложения был поврежден.");
                    alert.show();
                }
            }
            (((VBox) fewFilesResultsScene).getChildren().get(2)).setVisible(true);


            ((Accordion) (((VBox) fewFilesResultsScene).getChildren().get(2))).getPanes().clear();
            ((Accordion) (((VBox) fewFilesResultsScene).getChildren().get(2))).getPanes().addAll(panes);
        }

        private static void showGroupOfFileAnalysisIsGoingOn() {
            ((VBox) fewFilesResultsScene).getChildren().get(2).setVisible(false);
        }

        private static void showNoFileResults() {
            ProgressBar progressBar = (ProgressBar) ((VBox) (((BorderPane) oneFileResultsScene).getTop())).getChildren().get(1);
            TextFlow infoLabel = (TextFlow) ((VBox) (((BorderPane) oneFileResultsScene).getTop())).getChildren().get(0);

            Text t1 = new Text("Файл для обработки не выбран.");
            t1.getStyleClass().add("text-ordinary");
            infoLabel.getChildren().clear();
            infoLabel.getChildren().add(t1);
            progressBar.setVisible(false);
        }

        private static void showOneFileAnalysisResults() {
            ((BorderPane) oneFileResultsScene).getBottom().setVisible(true);
            if (((BorderPane) oneFileResultsScene).getCenter() != null) {
                ((BorderPane) oneFileResultsScene).getCenter().setVisible(true);
            }

            ProgressBar progressBar = (ProgressBar) ((VBox) (((BorderPane) oneFileResultsScene).getTop())).getChildren().get(1);
            TextFlow infoLabel = (TextFlow) ((VBox) (((BorderPane) oneFileResultsScene).getTop())).getChildren().get(0);

            showIntervals(0, true, null);

            HBox topInfoLabel = (HBox) ((VBox) (((BorderPane) oneFileResultsScene).getTop())).getChildren().get(2);

            HBox bottomBtns = (HBox) (((BorderPane) oneFileResultsScene).getBottom());
            bottomBtns.setVisible(true);

            topInfoLabel.setVisible(true);

            progressBar.setVisible(false);

            Text t11 = new Text("Обработка файла ");
            t11.getStyleClass().add("text-ordinary");
            Text t12 = new Text(data.getFilePath().get(0));
            t12.getStyleClass().add("text-blue-small");
            Text t13 = new Text(" завершена");
            t13.getStyleClass().add("text-ordinary");

            infoLabel.getChildren().clear();
            infoLabel.getChildren().addAll(t11, t12, t13);

            Label betti = (Label) topInfoLabel.getChildren().get(0);
            betti.setText(String.format("Числа Бетти: %s", data.getResults().getOnlyTDAResponse().getBettiNumbers()));

            ((BorderPane) oneFileResultsScene).setCenter(plotBarCode(0));

            ((Button) (bottomBtns.getChildren().get(0))).setOnAction(event -> {
                saveImg(true, null);
            });
        }

        private static ScrollPane plotBarCode(int id) {
            ScrollPane graphsScroll = new ScrollPane();

            VBox graphsPanel = new VBox();
            Set<Map.Entry<Integer, List<Interval>>> entrySet = data.getResults().getTdaResponse().get(id).getIntervals().entrySet();

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
                lineChart.setPrefHeight((entry.getValue().size() + 1) * 10);


                int i = 1;
                lineChart.setCreateSymbols(false);
                Collections.sort(entry.getValue(), Collections.reverseOrder());
                for (Interval in : entry.getValue()) {
                    XYChart.Series<Number, Number> series = new XYChart.Series<>();
                    series.getData().add(new XYChart.Data<>(in.getStart(), i));
                    if (!in.isRightInfinite()) {
                        XYChart.Data<Number, Number> right = new XYChart.Data<>(in.getEnd(), i);

                        series.getData().add(right);
                    } else {
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

            return graphsScroll;
        }

        private static void showOneFileAnalysisIsGoingOn() {
            TextFlow infoLabel = (TextFlow) ((VBox) (((BorderPane) oneFileResultsScene).getTop())).getChildren().get(0);
            HBox topInfoLabel = (HBox) ((VBox) (((BorderPane) oneFileResultsScene).getTop())).getChildren().get(2);

            if (((BorderPane) oneFileResultsScene).getCenter() != null) {
                ((BorderPane) oneFileResultsScene).getCenter().setVisible(false);
            }

            topInfoLabel.setVisible(false);

            Text t1 = new Text("Подождите, пока завершится анализ файла ");
            t1.getStyleClass().add("text-ordinary");
            Text t2 = new Text(data.getFilePath().get(0));
            t2.getStyleClass().add("text-blue-small");
            infoLabel.getChildren().clear();
            infoLabel.getChildren().addAll(t1, t2);

            ((BorderPane) oneFileResultsScene).setLeft(null);
            ((BorderPane) oneFileResultsScene).getBottom().setVisible(false);

        }

        private static void saveImg(boolean oneFile, BorderPane pane) {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("image files", "*.png");
            fileChooser.getExtensionFilters().add(extFilter);
            WritableImage image;
            if (oneFile) {
                image = ((ScrollPane) (((BorderPane) oneFileResultsScene).getCenter()))
                        .getContent()
                        .snapshot(new SnapshotParameters(), null);
            } else {
                image = ((ScrollPane) (pane.getCenter()))
                        .getContent()
                        .snapshot(new SnapshotParameters(), null);
            }
            File file = fileChooser.showSaveDialog(CourseWorkMain.MainLaunch.getPrimaryStage());
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static void showIntervals(int id, boolean oneFile, BorderPane pane) {
            Accordion intervalsAccordion = new Accordion();

            Set<Map.Entry<Integer, List<Interval>>> entrySet = CourseWorkMain.MainLaunch.data.getResults().getTdaResponse().get(id).getIntervals().entrySet();

            for (Map.Entry<Integer, List<Interval>> entry : entrySet) {
                TitledPane tp = new TitledPane();
                ScrollPane scroll = new ScrollPane();
                scroll.setPrefHeight(intervalsAccordion.getHeight());
                scroll.prefWidth(intervalsAccordion.getWidth());
                tp.setText(String.format("Dimension %d", entry.getKey()));

                VBox intervals = new VBox();
                for (Interval in : entry.getValue()) {
                    if (in.isRightInfinite()) {
                        intervals.getChildren().add(new Label(String.format("[%f, Infinity)", in.getStart())));
                    } else
                        intervals.getChildren().add(new Label(String.format("[%f, %f]", in.getStart(), in.getEnd())));
                }

                scroll.setContent(intervals);

                tp.setContent(scroll);
                intervalsAccordion.getPanes().add(tp);
            }

            if (!oneFile) {
                pane.setRight(intervalsAccordion);
            } else {
                ((BorderPane) CourseWorkMain.MainLaunch.oneFileResultsScene).setRight(intervalsAccordion);
            }
        }

        public static void updateLearningScene(File path) {

            List<String> directories = Arrays.asList(path.list((current, name) -> new File(current, name).isDirectory()));


//            boolean correctFolder = true;

            for (String el : new String[]{"AD_controls_corr_mats", "AD_controls_corr_mats_intime", "AD_patients_corr_mats", "AD_patients_corr_mats_intime"}) {
                if (!directories.contains(el)) {
//                    correctFolder = false;
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Выбранная папка не содержит требуемых данных");
                    alert.show();
                    return;
                }
            }

//            if (correctFolder) {
            Path pathToLearningData = Paths.get(path.toURI()).resolve("learning");
            Path pathToSettings = pathToLearningData.resolve(String.format("%s_%d_%s",
                    data.getComplexType(),
                    data.getMaxDimensions(),
                    String.valueOf(data.getMaxFiltrationValue()).replace(".", "_")));

            Label label = (Label) (((VBox) processScene).getChildren().get(3));


            if (Files.exists(pathToSettings)) {
                LoadJsonTask task = new LoadJsonTask(pathToSettings.toString());
                task.setOnSucceeded(event -> {
                    label.textProperty().unbind();
                    label.setText("Данные, полученные во время обучения ранее, загружены.");
                    data.setLearningData(task.getValue());
                });

                new Thread(task).start();
            } else {
                ProgressBar progressBar = (ProgressBar) ((VBox) processScene).getChildren().get(2);

                progressBar.setVisible(true);
                progressBar.setProgress(0);

                data.setLearningDataFolder(path.getAbsolutePath());
                ProcessLearningDataTask task = new ProcessLearningDataTask();

                progressBar.progressProperty().unbind();
                progressBar.progressProperty().bind(task.progressProperty());

                label.textProperty().unbind();
                label.textProperty().bind(task.messageProperty());

                task.setOnSucceeded(event -> {
                    label.textProperty().unbind();
                    label.setText(String.format("Обучение завершено. Данные сохранены %s", task.getMessage()));
                    data.setLearningData(task.getValue());
                    progressBar.setVisible(false);
                });

                new Thread(task).start();
            }
        }


        @Override
        public void start(Stage primaryStage) throws Exception {

            setPrimaryStage(primaryStage);

            root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
            primaryStage.setTitle("Connectomes");
            Scene scene = new Scene(root, 1200, 800);

            Button currentMainButton = new Button();

            for (Node n : ((VBox) (((BorderPane) root).getLeft())).getChildren()) {
                if (n.getId().equals("settings")) {
                    currentMainButton = (Button) n;
                }
            }
            currentMainButton.setMaxHeight(Double.MAX_VALUE);

            VBox.setVgrow(currentMainButton, Priority.ALWAYS);

            primaryStage.setScene(scene);
            primaryStage.show();
        }


    }

}

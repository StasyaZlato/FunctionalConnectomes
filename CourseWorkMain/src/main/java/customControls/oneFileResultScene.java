//package customControls;
//
//import javafx.concurrent.Task;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.scene.text.Text;
//import javafx.scene.text.TextFlow;
//import main.CourseWorkMain;
//import pojo.TDAResponse;
//import processing.ProcessFile;
//
//public class oneFileResultScene extends BorderPane {
//    VBox pageTop;
//    HBox results;
//
//    TextFlow infoLabel;
//
//    public oneFileResultScene() {
//        if (CourseWorkMain.MainLaunch.data.noFilePathGiven()) {
//            Text t1 = new Text("Файл для обработки не выбран.");
//            t1.getStyleClass().add("text-ordinary");
//            infoLabel.getChildren().clear();
//            infoLabel.getChildren().add(t1);
//            progressBar.setVisible(false);
//        }
//            if (CourseWorkMain.MainLaunch.data.getResults().getTdaResponse().isEmpty() ||
//                    !CourseWorkMain.MainLaunch.data.getFilePath().equals(CourseWorkMain.MainLaunch.data.getResults().getTdaResponse().get(0).getPath())) {
//
//                Text t1 = new Text("Подождите, пока завершится анализ файла ");
//                t1.getStyleClass().add("text-ordinary");
//                Text t2 = new Text(data.getFilePath());
//                t2.getStyleClass().add("text-blue-small");
//                infoLabel.getChildren().clear();
//                infoLabel.getChildren().addAll(t1, t2);
//
//                ProcessFile processFile = new ProcessFile(data.getFilePath());
//                progressBar.setVisible(true);
//                Task<TDAResponse> task = new Task<TDAResponse>() {
//                    @Override
//                    public TDAResponse call() {
//                        return processFile.process2DArray();
//                    }
//                };
//
//                task.setOnSucceeded(e -> {
//                    TDAResponse result = task.getValue();
//                    data.setResults(result);
//                    progressBar.setVisible(false);
//
//                    Text t11 = new Text("Обработка файла ");
//                    t11.getStyleClass().add("text-ordinary");
//                    Text t12 = new Text(data.getFilePath());
//                    t12.getStyleClass().add("text-blue-small");
//                    Text t13 = new Text(" завершена");
//                    t13.getStyleClass().add("text-ordinary");
//
//                    infoLabel.getChildren().clear();
//                    infoLabel.getChildren().addAll(t11, t12, t13);
//                });
//
//                progressBar.progressProperty().bind(task.progressProperty());
//
//
//                new Thread(task).start();
//            }
//        }
//
//    }
//
//}

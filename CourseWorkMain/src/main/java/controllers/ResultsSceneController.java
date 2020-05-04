package controllers;

import edu.stanford.math.plex4.visualization.BarcodeVisualizer;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import main.CourseWorkMain;
import pojo.Interval;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResultsSceneController {

    @FXML
    public Button showIntervalsBtn;
    @FXML
    public VBox graphVBox;

    boolean intervalsShown = false;

    public void showIntervals(ActionEvent actionEvent) {
        if (!intervalsShown) {
            Accordion intervalsAccordion = new Accordion();

            Set<Map.Entry<Integer, List<Interval>>> entrySet = CourseWorkMain.MainLaunch.data.getResults().getOnlyTDAResponse().getIntervals().entrySet();

            for (Map.Entry<Integer, List<Interval>> entry : entrySet) {
                TitledPane tp = new TitledPane();
                ScrollPane scroll = new ScrollPane();
                scroll.setPrefHeight(intervalsAccordion.getHeight());
                scroll.prefWidth(intervalsAccordion.getWidth());
                tp.setText(String.format("Dimension %d", entry.getKey()));

                VBox intervals = new VBox();
                for (Interval in : entry.getValue()) {
                    if (in.isLeftInfinite()) {
                        intervals.getChildren().add(new Label(String.format("[%f, Infinity)", in.getStart())));
                    } else
                        intervals.getChildren().add(new Label(String.format("[%f, %f]", in.getStart(), in.getEnd())));
                }

                scroll.setContent(intervals);

                tp.setContent(scroll);
                intervalsAccordion.getPanes().add(tp);
            }
            ((BorderPane) CourseWorkMain.MainLaunch.oneFileResultsScene).setRight(intervalsAccordion);
            showIntervalsBtn.setText("Скрыть интервалы");
            intervalsShown = true;
        }
        else {
            ((BorderPane) CourseWorkMain.MainLaunch.oneFileResultsScene).setRight(null);
            intervalsShown = false;
            showIntervalsBtn.setText("Посмотреть интервалы");
        }
    }

    public void saveImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("image files", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        WritableImage image = graphVBox.snapshot(new SnapshotParameters(), null);
        File file = fileChooser.showSaveDialog(CourseWorkMain.MainLaunch.getPrimaryStage());
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

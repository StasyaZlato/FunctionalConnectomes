package controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.DirectoryChooser;
import main.CourseWorkMain;

import java.io.File;

public class ProcessSceneController {


    public void chooseLearningData(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File chosenDirectory = directoryChooser.showDialog(((Node) actionEvent.getSource()).getScene().getWindow());

        if (chosenDirectory != null) {
            CourseWorkMain.MainLaunch.updateLearningScene(chosenDirectory);
        }
    }


}

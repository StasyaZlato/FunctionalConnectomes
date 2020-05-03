package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.CourseWorkMain;
import sun.rmi.rmic.Main;

import java.io.File;

public class UserInputSceneController {
    @FXML
    TextFlow currentSettings;

    public void chooseOneFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Numpy arrays", "*.npy");
        fileChooser.getExtensionFilters().add(extFilter);
        File chosenFile = fileChooser.showOpenDialog(((Node)actionEvent.getSource()).getScene().getWindow());
        if (chosenFile != null) {
            CourseWorkMain.MainLaunch.data.setFilePath(chosenFile.getAbsolutePath());
        }
        changeResultsButton();
        CourseWorkMain.MainLaunch.openResults();
    }

    public void chooseFewFiles(ActionEvent actionEvent) {

    }

    private void changeResultsButton() {
        ObservableList<Node> buttonList =  ((VBox)(((BorderPane) CourseWorkMain.MainLaunch.root).getLeft())).getChildren();
        Button[] buttons = buttonList.stream().map(n -> (Button)n).toArray(Button[]::new);
        VBox.setVgrow(buttons[3], Priority.ALWAYS);
        buttons[3].setMaxHeight(Double.MAX_VALUE);
        buttons[3].getStyleClass().clear();
        buttons[3].getStyleClass().add("active");
        buttons[3].getStyleClass().add("button");

        for (int i = 0; i < 3; i++) {
            VBox.setVgrow(buttons[i], Priority.NEVER);
            buttons[i].getStyleClass().clear();
            buttons[i].getStyleClass().add("passive");
            buttons[i].getStyleClass().add("button");

        }
    }
}

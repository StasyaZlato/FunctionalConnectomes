package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import main.CourseWorkMain;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class UserInputSceneController {
    @FXML
    TextFlow currentSettings;

    public void chooseOneFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Numpy arrays", "*.npy");
        fileChooser.getExtensionFilters().add(extFilter);
        File chosenFile = fileChooser.showOpenDialog(((Node) actionEvent.getSource()).getScene().getWindow());
        if (chosenFile != null) {
            CourseWorkMain.MainLaunch.data.setFilePath(chosenFile.getAbsolutePath());
        }
        changeResultsButton();
        CourseWorkMain.MainLaunch.openResults();
    }

    public void chooseFewFiles(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Numpy arrays", "*.npy");
        fileChooser.getExtensionFilters().add(extFilter);
        List<File> chosenFile = fileChooser.showOpenMultipleDialog(((Node) actionEvent.getSource()).getScene().getWindow());

        List<String> paths = chosenFile.stream().map(File::getAbsolutePath).collect(Collectors.toList());

        if (!chosenFile.isEmpty()) {
            CourseWorkMain.MainLaunch.data.setFilePath(paths);
        }
        changeResultsButton();
        CourseWorkMain.MainLaunch.openResults();
    }

    private void changeResultsButton() {
        ObservableList<Node> buttonList = ((VBox) (((BorderPane) CourseWorkMain.MainLaunch.root).getLeft())).getChildren();
        Button[] buttons = buttonList.stream().map(n -> (Button) n).toArray(Button[]::new);
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

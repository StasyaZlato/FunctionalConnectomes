package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import main.CourseWorkMain;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResultsSceneController {

    public void saveResults(ActionEvent actionEvent) throws IOException {

        byte[] json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(CourseWorkMain.MainLaunch.data).getBytes();

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("json", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(CourseWorkMain.MainLaunch.getPrimaryStage());
        try {
            Files.write(Paths.get(file.toURI()), json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

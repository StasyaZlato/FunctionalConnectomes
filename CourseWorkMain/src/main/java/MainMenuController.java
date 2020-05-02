import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainMenuController {

    @FXML
    Button settings;

    @FXML
    Button process;

    @FXML
    Button open;

    @FXML
    Button results;

    public void openNextTab(MouseEvent mouseEvent) throws IOException {
        Button b = (Button)(mouseEvent.getSource());

        switch (b.getId()) {
            case "settings":
                buttonDesignChange(0);
                CourseWorkMain.MainLaunch.openSettings();
                break;
            case "process":
                buttonDesignChange(1);
                CourseWorkMain.MainLaunch.openProcess();
                break;
            case "open":
                buttonDesignChange(2);
                CourseWorkMain.MainLaunch.openUserInput();
                break;
            case "results":
                buttonDesignChange(3);
                CourseWorkMain.MainLaunch.openResults();
                break;
        }
    }

    private void buttonDesignChange(int id) {
        Button[] buttons = {settings, process, open, results};
        VBox.setVgrow(buttons[id], Priority.ALWAYS);
        buttons[id].setMaxHeight(Double.MAX_VALUE);
        buttons[id].getStyleClass().clear();
        buttons[id].getStyleClass().add("active");
        buttons[id].getStyleClass().add("button");

        for (int i = 0; i < 4; i++) {
            if (i != id) {
                VBox.setVgrow(buttons[i], Priority.NEVER);
                buttons[i].getStyleClass().clear();
                buttons[i].getStyleClass().add("passive");
                buttons[i].getStyleClass().add("button");
            }
        }
    }
}








package controllers;

import customControls.Toast;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import main.CourseWorkMain;

import javax.swing.*;
import javafx.event.ActionEvent;


public class SettingsController {
    @FXML
    ChoiceBox<String> complexType;

    @FXML
    TextField maxFiltrationValue;

    @FXML
    ChoiceBox<String> maxDimensions;

    @FXML
    void saveSettings(ActionEvent e) {
        Double maxFiltrationValueD;
        Integer maxDimensionsI;


        String maxFiltrationStr = maxFiltrationValue.getText();
        if (maxFiltrationStr.contains(",")) {
            maxFiltrationStr = maxFiltrationStr.replaceAll(",", ".");
        }
        if (maxFiltrationStr.isEmpty()) {
            maxFiltrationValueD = 1.0;
        }
        else {
            maxFiltrationValueD = Double.valueOf(maxFiltrationStr);
        }

        CourseWorkMain.MainLaunch.data.setComplexType(complexType.getValue());
        CourseWorkMain.MainLaunch.data.setMaxFiltrationValue(maxFiltrationValueD);
        CourseWorkMain.MainLaunch.data.setMaxDimensions(Integer.parseInt(maxDimensions.getValue()));

        String toastMsg = "Настройки установлены!";
        int toastMsgTime = 3500; //3.5 seconds
        int fadeInTime = 500; //0.5 seconds
        int fadeOutTime= 500; //0.5 seconds
        Toast.makeText((Stage)((Node)e.getSource()).getScene().getWindow(), toastMsg, toastMsgTime, fadeInTime, fadeOutTime);
    }

}

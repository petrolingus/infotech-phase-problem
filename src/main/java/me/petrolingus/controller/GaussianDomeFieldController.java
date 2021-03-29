package me.petrolingus.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class GaussianDomeFieldController implements Initializable {

    @FXML
    private Label title;

    @FXML
    private TextField amplitudeField;

    @FXML
    private TextField meanField;

    @FXML
    private TextField deviationField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public Label getTitle() {
        return title;
    }

    public TextField getAmplitudeField() {
        return amplitudeField;
    }

    public TextField getMeanField() {
        return meanField;
    }

    public TextField getDeviationField() {
        return deviationField;
    }
}

package me.petrolingus.control;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import me.petrolingus.controller.GaussianDomeFieldController;

import java.io.IOException;

public class GaussianDomeField extends VBox {

    GaussianDomeFieldController controller;

    public GaussianDomeField() {

        super();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GaussianDomeField.fxml"));
            controller = new GaussianDomeFieldController();
            loader.setController(controller);
            Node node = loader.load();

            this.getChildren().add(node);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

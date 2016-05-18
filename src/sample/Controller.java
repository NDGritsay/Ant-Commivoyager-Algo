package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

import  antDraw.antDraw;

public class Controller implements Initializable {
    @FXML
    Button mainButton;
    @FXML
    Slider sliderAlpha;
    @FXML
    Slider sliderBetta;
    @FXML
    Slider sliderP;
    @FXML
    Slider sliderN;
    @FXML
    Slider sliderSpeed;
    @FXML
    Slider sliderK;
    @FXML
    Pane pane;

    private antDraw antThread = new antDraw();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sliderN.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                antDraw.changeLineValuesTable((int) sliderN.getValue());
                antDraw.generateLineValuesLabels();
                antDraw.generateVertexes();
                antDraw.generateEdges();
            }
        });

        antDraw.pane = pane;
        antDraw.generateLineValues((int) sliderN.getValue());
        antDraw.generateLineValuesLabels();
        antDraw.generateVertexes();
        antDraw.generateEdges();
    }

    @FXML
    public void startButton() {
        //проверка ввода

        if (!antThread.isAlive()) {
            sliderAlpha.setDisable(true);
            sliderBetta.setDisable(true);
            sliderK.setDisable(true);
            sliderN.setDisable(true);
            sliderP.setDisable(true);
            antDraw.startLineValues();
            mainButton.setText("Stop");

            antDraw.setConstants(sliderAlpha.getValue(), sliderBetta.getValue(), sliderP.getValue(),
                    (int) sliderK.getValue(), (int) sliderSpeed.getValue());
            antThread = new antDraw();
            antThread.start();
        } else {
            sliderAlpha.setDisable(false);
            sliderBetta.setDisable(false);
            sliderK.setDisable(false);
            sliderN.setDisable(false);
            sliderP.setDisable(false);
            antDraw.stopLineValues();
            mainButton.setText("Start");

            antDraw.isFinished = true;
            pane.getChildren().remove(antDraw.shape);
        }
    }
}

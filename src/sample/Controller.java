package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
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
        antDraw.initialize((int) sliderN.getValue(), sliderSpeed);
        antDraw.generateLineValuesLabels();
        antDraw.generateVertexes();
        antDraw.generateEdges();
    }

    @FXML
    public void startButton() {
        //проверка ввода

        if (!antThread.isAlive()) {
            if(antDraw.lineValuesCheck()) {
                sliderAlpha.setDisable(true);
                sliderBetta.setDisable(true);
                sliderK.setDisable(true);
                sliderN.setDisable(true);
                sliderP.setDisable(true);
                antDraw.startLineValues();
                antDraw.mirrorLineValues();
                mainButton.setText("Stop");

                antDraw.setConstants(sliderAlpha.getValue(), sliderBetta.getValue(), sliderP.getValue(),
                        (int) sliderK.getValue(), (int) sliderSpeed.getValue());
                antThread = new antDraw();
                antThread.start();
            }
            else {
                mainButton.setText("wrong input!");
                try{
                    Thread.sleep(5000);
                }
                catch (InterruptedException exc){
                    System.out.println("ops");
                }
                mainButton.setText("Start");
            }
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

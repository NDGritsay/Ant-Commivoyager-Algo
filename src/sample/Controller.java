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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sliderN.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                antDraw.changeLineValuesTable((int) sliderN.getValue());
                antDraw.generateVertexes();
                antDraw.generateEdges();
            }
        });

        antDraw.pane = pane;
        antDraw.generateLineValues((int) sliderN.getValue());
        antDraw.generateVertexes();
        antDraw.generateEdges();
    }

    @FXML
    public void startButton() {
        //проверка ввода
        try {
            antDraw.run(sliderAlpha.getValue(), sliderBetta.getValue(), sliderP.getValue(),
                    (int) sliderK.getValue(), sliderSpeed);
        } catch (InterruptedException exc) {
            System.out.println("lol");
        }
    }
}

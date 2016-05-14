package antDraw;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

/**
 * Created by Nikita Gritsay on 13.05.2016.
 */
public class antDraw {
    public static TextField[][] lineValues;
    public static Pane pane;
    public static final int cityMax = 8;
    private static int cityX;
    public static int cityCt;

    public static void generateLineValues(int cityCt){
        antDraw.cityCt = cityCt;
        cityX = cityMax - cityCt;
        lineValues = new TextField[cityMax][cityMax];
        for(int i = 0; i < cityMax; i++)
            for(int j = 0; j < cityMax; j++){
                lineValues[i][j] = new TextField();
                pane.getChildren().add(lineValues[i][j]);
                lineValues[i][j].setId(Integer.toString(i) + Integer.toString(j));
                lineValues[i][j].setLayoutX(716 + j * 38);
                lineValues[i][j].setLayoutY(222 + i * 26);
                lineValues[i][j].setPrefWidth(36);
                lineValues[i][j].setPrefHeight(24);
            }
        changeLineValuesTable(cityCt);
    }

    public static void changeLineValuesTable(int cityCt){
        antDraw.cityCt = cityCt;
        cityX = cityMax - cityCt;
        for(int i = 0; i < cityCt; i++)
            for(int j = cityX; j < cityMax; j++) {
                lineValues[i][j].setVisible(true);
                lineValues[i][j].setDisable(false);
            }
        for(int j = cityX - 1; j >= 0; j--)
            for(int i = 0; i < cityMax; i++)
                lineValues[i][j].setVisible(false);
        for(int i = cityCt; i < cityMax; i++)
            for(int j = cityX; j < cityMax; j++)
                lineValues[i][j].setVisible(false);
        for(int i = 0; i < cityCt; i++)
            for(int j = cityX + i; j < cityMax; j++)
                lineValues[i][j].setDisable(true);
        for(int i = 0; i < cityCt; i++)
            lineValues[i][cityX + i].setText("");
        for(int i = 1; i < cityCt; i++)
            for(int j = 0; j < i; j++)
                lineValues[j][cityX+i].setText(/*lineValues[i][cityX+j].getText()*/"");
    }
}

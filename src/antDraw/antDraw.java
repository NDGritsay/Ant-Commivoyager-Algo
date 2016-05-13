package antDraw;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * Created by Nikita Gritsay on 13.05.2016.
 */
public class antDraw {
    public static TextField[][] lineValues;
    public static Pane pane;
    public static final int cityMax = 8;

    public static void generateLineValues(){
        lineValues = new TextField[cityMax][cityMax];
        for(int i = 0; i < cityMax; i++)
            for(int j = 0; j < cityMax; j++){
                lineValues[i][j] = new TextField();
                pane.getChildren().add(lineValues[i][j]);
                lineValues[i][j].setLayoutX(716 + i * 38);
                lineValues[i][j].setLayoutY(222 + j * 26);
                lineValues[i][j].setPrefWidth(36);
                lineValues[i][j].setPrefHeight(24);
            }
    }
}

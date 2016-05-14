package Ant;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

/**
 * Created by Nikita Gritsay on 14.05.2016.
 */
public class Ant {
    private Pane pane;
    private Circle shape; //фигура муравья
    private double[][] originPhero;  //общая матрица феромонов
    private double[][] phero;  //матрица феромонов на момент старт муравья
    private int [][] lineValues;  //матрица расстояний до городов
    private int cityCt;  //кол-во городов
    private int[] blackList;  //пройденные муравьем города
    private double alpha;
    private double betta;
    private int lenLeft;  //оставшийся путь до города
    private int[] cityXCoords;  //координаты Х городов
    private int[] cityYCoords;  //координаты У городов
    public boolean isFinished;

    public Ant(Pane pane, double[][] phero, int[][] lineValues, int cityCt, double alpha,
               double betta, int[] cityXCoords, int[] cityYCoords){
        this.pane = pane;
        this.originPhero = phero;
        this.lineValues = lineValues;
        this.cityCt = cityCt;
        this.blackList = new int[cityCt];
        this.alpha = alpha;
        this.betta = betta;
        this.cityXCoords = cityXCoords;
        this.cityYCoords = cityYCoords;
        this.lenLeft = 0;
        this.isFinished = false;
        this.phero = phero.clone();
    }


}

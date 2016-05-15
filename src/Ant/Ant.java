package Ant;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Material;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

/**
 * Created by Nikita Gritsay on 14.05.2016.
 */
public class Ant {
    private Pane pane;
    private Circle shape; //фигура муравья
    private double[][] phero;  //матрица феромонов
    private int [][] lineValues;  //матрица расстояний до городов
    private int cityCt;  //кол-во городов
    private ArrayList<Integer> history;  //пройденные муравьем города
    private ArrayList<Integer> whiteList;  //доступные города
    private double alpha;
    private double betta;
    private int lenLeft;  //оставшийся путь до города
    private int[] cityXCoords;  //координаты Х городов
    private int[] cityYCoords;  //координаты У городов
    public boolean isFinished;
    private boolean inCity;
    private int currentCity;
    private int prevCity;

    public Ant(Pane pane, double[][] phero, int[][] lineValues, int cityCt, double alpha,
               double betta, int[] cityXCoords, int[] cityYCoords){
        this.pane = pane;
        this.lineValues = lineValues;
        this.cityCt = cityCt;
        this.history = new ArrayList<>(cityCt + 1);
        this.history.add(0);
        this.whiteList = new ArrayList<>(cityCt - 1);
        for(int i = 0; i < cityCt - 1; i++)
            whiteList.add(i + 1);
        this.alpha = alpha;
        this.betta = betta;
        this.cityXCoords = cityXCoords;
        this.cityYCoords = cityYCoords;
        this.lenLeft = 0;
        this.isFinished = false;
        this.phero = phero;
        this.inCity = true;
        this.currentCity = 0;
        //add shape initialize
    }

    private double procDenominator(){
        double denominator = 0;
        for(int i = 0; i < whiteList.size(); i++)
            denominator += Math.pow(phero[currentCity][whiteList.get(i)], alpha) *
                    Math.pow((1/lineValues[currentCity][whiteList.get(i)]), betta);
        return  denominator;
    }

    private double procProbability(int cityId, double denominator){
        return Math.pow(phero[currentCity][cityId], alpha) *
                Math.pow((1/lineValues[currentCity][cityId]), betta) / denominator;
    }

    private int getRandomId(double probabilities[]){
        int cityId = -1;
        double marks[] = new double[probabilities.length];
        marks[0] = probabilities[0];
        for(int i = 1; i < probabilities.length; i++)
            marks[i] = marks[i-1] + probabilities[i];
        double randomMark = Math.random() * marks[marks.length - 1]; //последний элемент marks == сумме всех вероятностей
        for(int i = 0; i < marks.length; i++)
            if(randomMark <= marks[i])
                cityId = i;
        return cityId;
    }

    private void toNextCity(){
        int nextCityId;

        prevCity = currentCity;
        history.add(prevCity);

        if(whiteList.size() > 0) {
            double denominator = procDenominator();
            double cityProbabilities[] = new double[whiteList.size()];

            for(int i = 0; i < whiteList.size(); i++)
                cityProbabilities[i] = procProbability(whiteList.get(i), denominator);
            nextCityId = getRandomId(cityProbabilities);

            currentCity = whiteList.get(nextCityId);
            whiteList.remove(nextCityId);
        }
        else
            currentCity = 0;

        lenLeft = lineValues[prevCity][currentCity];
        inCity = false;
    }

    private void move(){

    }

    public void round() {
        if (inCity)
            if (currentCity == 0 && history.size() == cityCt + 1) {
                //обновить феромоны
                isFinished = true;
            }
            else{
                toNextCity();
            }
        else
            move();

    }
}

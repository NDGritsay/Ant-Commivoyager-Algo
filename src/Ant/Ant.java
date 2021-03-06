package Ant;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

/**
 * Created by Nikita Gritsay on 14.05.2016.
 */
public class Ant {
    private Pane pane;
    private Circle shape; //фигура муравья
    private double[][] pheros;  //матрица феромонов
    private int [][] lineValues;  //матрица расстояний до городов
    private int cityCt;  //кол-во городов
    private ArrayList<Integer> history;  //пройденные муравьем города
    private ArrayList<Integer> whiteList;  //доступные города
    private double alpha;
    private double betta;
    private double p;
    private int k;
    private int lenLeft;  //оставшийся путь до города
    private int lenTotal;
    private int[] cityXCoords;  //координаты Х городов
    private int[] cityYCoords;  //координаты У городов
    public boolean isFinished;
    private boolean inCity;
    private int currentCity;
    private int prevCity;
    private Slider speedSlider;
    private final int FPS = 70;

    public Ant(Pane pane, double[][] pheros, int[][] lineValues, int cityCt, double alpha,
               double betta,double p, int k, Slider speedSlider, int[] cityXCoords, int[] cityYCoords, Circle shape){
        this.pane = pane;
        this.lineValues = lineValues;
        this.cityCt = cityCt;
        this.history = new ArrayList<>(cityCt + 1);
        this.whiteList = new ArrayList<>(cityCt - 1);
        for(int i = 0; i < cityCt - 1; i++)
            whiteList.add(i + 1);
        this.alpha = alpha;
        this.betta = betta;
        this.p = p;
        this.k = k;
        this.cityXCoords = cityXCoords;
        this.cityYCoords = cityYCoords;
        this.lenLeft = 0;
        this.isFinished = false;
        this.pheros = pheros;
        this.inCity = true;
        this.currentCity = 0;
        this.shape = shape;
        this.speedSlider = speedSlider;
    }

    private double procDenominator(){
        double denominator = 0;
        for(int i = 0; i < whiteList.size(); i++)
            denominator += Math.pow(pheros[currentCity][whiteList.get(i)], alpha) *
                    Math.pow((1/(double)lineValues[currentCity][whiteList.get(i)]), betta);
        return  denominator;
    }

    private double procProbability(int cityId, double denominator){
        return Math.pow(pheros[currentCity][cityId], alpha) *
                Math.pow((1/(double)lineValues[currentCity][cityId]), betta) / denominator;
    }

    private int getRandomId(double probabilities[]){
        int cityId = -1;
        double marks[] = new double[probabilities.length];
        marks[0] = probabilities[0];
        for(int i = 1; i < probabilities.length; i++)
            marks[i] = marks[i-1] + probabilities[i];
        double randomMark = Math.random() * marks[marks.length - 1]; //последний элемент marks == сумме всех вероятностей
        do{
            cityId++;
        }while(randomMark > marks[cityId]);
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

        lenTotal = lenLeft = lineValues[prevCity][currentCity];
        inCity = false;
    }

    /*
    private void move(){
        lenLeft--;
        if(lenLeft == 0) {
            inCity = true;
            shape.setCenterX(cityXCoords[currentCity]);
            shape.setCenterY(cityYCoords[currentCity]);
        }
        else{
            shape.setCenterX(cityXCoords[prevCity] + (lenTotal - lenLeft) / (double)lenTotal *
                    (cityXCoords[currentCity] - cityXCoords[prevCity]));
            shape.setCenterY(cityYCoords[prevCity] + (lenTotal - lenLeft) / (double)lenTotal *
                    (cityYCoords[currentCity] - cityYCoords[prevCity]));
        }
    }*/

    private void move(){
        shape.setCenterX(cityXCoords[prevCity]);
        shape.setCenterY(cityYCoords[prevCity]);

        double deltaX, deltaY, moveTime;
        int stepCt;

        moveTime = lineValues[currentCity][prevCity] / speedSlider.getValue();
        stepCt = (int)(moveTime * FPS);
        deltaX = (cityXCoords[currentCity] - cityXCoords[prevCity]) / (double)stepCt;
        deltaY = (cityYCoords[currentCity] - cityYCoords[prevCity]) / (double)stepCt;

        for(int i = 0; i < stepCt; i++){
            shape.setCenterX(shape.getCenterX() + deltaX);
            shape.setCenterY(shape.getCenterY() + deltaY);
            try {
                Thread.sleep(1000 / FPS);
            }
            catch (InterruptedException exc){
                System.out.println("lol");
            }
        }

        inCity = true;
        shape.setCenterX(cityXCoords[currentCity]);
        shape.setCenterY(cityYCoords[currentCity]);
    }

    private void pherosUpdate(){
        int allDistance = 0;
        for(int i = 0; i < history.size() - 1; i++)
            allDistance += lineValues[history.get(i)][history.get(i+1)];

        for(int i = 0; i < pheros.length; i++)
            for(int j = 0; j < pheros.length; j++)
                pheros[i][j] *= 1 - p;

        for(int i = 0; i < history.size() - 1; i++){
            int city1 = history.get(i), city2 = history.get(i + 1);
            pheros[city1][city2] = pheros[city2][city1] += k / (double)allDistance;
        }
    }


    public void round() {
        if (inCity)
            if (currentCity == 0 && history.size() > 0) {
                history.add(0);
                pherosUpdate();
                isFinished = true;
            }
            else{
                toNextCity();
            }
        else
            move();
    }
}

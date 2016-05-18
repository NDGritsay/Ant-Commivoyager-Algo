package antDraw;

import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

import Ant.*;

/**
 * Created by Nikita Gritsay on 13.05.2016.
 */
public class antDraw extends Thread{
    public static Pane pane;
    public static TextField[][] lineValues;
    public static Circle[] vertexes;
    public static Line[][] edges;
    public static double[][] pheros;
    public static boolean isFinished = false;
    public static final int CITY_MAX = 8;
    public static final int MAP_CENTRE_X = 360;
    public static final int MAP_CENTRE_Y = 300;
    public static final int MAP_RADIUS = 250;
    public static final int VERTEX_RADIUS = 25;
    public static final int EDGES_WIDTH = 10;
    public static final int PHERO_START_VALUE = 10;
    public static double alpha;
    public static double betta;
    public static double p;
    public static int speed;
    public static int k;
    private static int cityX;
    public static int cityCt;
    public static Circle shape;

    public static void generateLineValues(int cityCt){
        antDraw.cityCt = cityCt;
        cityX = CITY_MAX - cityCt;
        lineValues = new TextField[CITY_MAX][CITY_MAX];
        for(int i = 0; i < CITY_MAX; i++)
            for(int j = 0; j < CITY_MAX; j++){
                lineValues[i][j] = new TextField();
                pane.getChildren().add(lineValues[i][j]);
                lineValues[i][j].setId(Integer.toString(i) + Integer.toString(j));
                lineValues[i][j].setLayoutX(716 + j * 38);
                lineValues[i][j].setLayoutY(270 + i * 26);
                lineValues[i][j].setPrefWidth(36);
                lineValues[i][j].setPrefHeight(24);
            }
        changeLineValuesTable(cityCt);
    }

    public static void changeLineValuesTable(int cityCt){
        antDraw.cityCt = cityCt;
        cityX = CITY_MAX - cityCt;
        for(int i = 0; i < cityCt; i++)
            for(int j = cityX; j < CITY_MAX; j++) {
                lineValues[i][j].setVisible(true);
                lineValues[i][j].setDisable(false);
            }
        for(int j = cityX - 1; j >= 0; j--)
            for(int i = 0; i < CITY_MAX; i++)
                lineValues[i][j].setVisible(false);
        for(int i = cityCt; i < CITY_MAX; i++)
            for(int j = cityX; j < CITY_MAX; j++)
                lineValues[i][j].setVisible(false);
        for(int i = 0; i < cityCt; i++)
            for(int j = cityX + i; j < CITY_MAX; j++)
                lineValues[i][j].setDisable(true);
        for(int i = 0; i < cityCt; i++)
            lineValues[i][cityX + i].setText("");
        for(int i = 1; i < cityCt; i++)
            for(int j = 0; j < i; j++)
                lineValues[j][cityX+i].setText(/*lineValues[i][cityX+j].getText()*/"");
    }

    public static void generateVertexes(){
        if(vertexes != null)
            for(int i = 0; i < vertexes.length; i++)
                pane.getChildren().remove(vertexes[i]);
        vertexes = new Circle[cityCt];
        for(int i = 0; i < cityCt; i++){
            vertexes[i] = new Circle();
            vertexes[i].setCenterX(MAP_CENTRE_X + MAP_RADIUS * Math.cos(2 * Math.PI / cityCt * i));
            vertexes[i].setCenterY(MAP_CENTRE_Y + MAP_RADIUS * Math.sin(2 * Math.PI / cityCt * i));
            vertexes[i].setRadius(VERTEX_RADIUS);
            vertexes[i].setFill(i != 0 ? Color.BLUE : Color.GOLD);
            pane.getChildren().add(vertexes[i]);
        }
    }

    public static void generateEdges(){
        pheros = new double[cityCt][cityCt];
        for(int i = 0; i < pheros.length; i++)
            for(int j = 0; j < pheros[i].length; j++)
                pheros[i][j] = PHERO_START_VALUE;

        if(edges != null)
            for(int i = 1; i < edges.length; i++)
                for(int j = 0; j < edges[i].length; j++)
                    pane.getChildren().remove(edges[i][j]);
        edges = new Line[cityCt][];
        for(int i = 1; i < cityCt; i++){
            edges[i] = new Line[i];
            for(int j = 0; j < edges[i].length; j++){
                edges[i][j] = new Line(vertexes[i].getCenterX(), vertexes[i].getCenterY(),
                        vertexes[j].getCenterX(), vertexes[j].getCenterY());
                edges[i][j].setStrokeWidth(EDGES_WIDTH);
                edges[i][j].setStroke(Color.PURPLE);
                edges[i][j].setStrokeLineCap(StrokeLineCap.ROUND);
                pane.getChildren().add(edges[i][j]);
            }
        }
    }

    public static void setConstants(double alpha, double betta, double p, int k, int speed){
        antDraw.alpha = alpha;
        antDraw.betta = betta;
        antDraw.p = p;
        antDraw.k = k;
        antDraw.speed = speed;

        if(shape != null)
            pane.getChildren().remove(shape);
        antDraw.shape = new Circle(0, 0,
                10, Color.BLACK);
        pane.getChildren().add(shape);
    }

    //change
    public static int pheroToWidth(int phero){
        if(phero > 30)
            return 30;
        else
            return phero;
    }

    //change
    public static boolean lineValuesCheck(){
        return true;
    }

    public void run(){
        int[][] lineValues = new int[cityCt][cityCt];
        for(int i = 1; i < cityCt; i++)
            for(int j = 0; j < i; j++)
                lineValues[i][j] = lineValues[j][i] = Integer.parseInt(
                        antDraw.lineValues[i][j + cityX].getText());

        /*//output linevales
        for(int i = 0; i < cityCt; i++) {
            for (int j = 0; j < cityCt; j++)
                if (i != j)
                    System.out.println(lineValues[i][j]);
            System.out.println();
        }*/

        int[] cityXCoords = new int[cityCt];
        for(int i = 0; i < cityCt; i++)
            cityXCoords[i] = (int)vertexes[i].getCenterX();

        int[] cityYCoords = new int[cityCt];
        for(int i = 0; i < cityCt; i++)
            cityYCoords[i] = (int)vertexes[i].getCenterY();

        shape.setCenterX(cityXCoords[0]);
        shape.setCenterY(cityYCoords[0]);

        do {
            Ant ant = new Ant(antDraw.pane, pheros, lineValues, cityCt, alpha,
                    betta, p, k, cityXCoords, cityYCoords, shape);

            do {
                ant.round();
                try {
                    Thread.sleep(1000 - antDraw.speed);
                } catch (InterruptedException e) {
                    System.out.println("ops!");
                }
            } while (!ant.isFinished);
        }while(!antDraw.isFinished);
    }
}
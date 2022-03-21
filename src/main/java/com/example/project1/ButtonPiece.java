package com.example.project1;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ButtonPiece {

    private double radius;
    private double x;
    private double y;
    private Circle circle;
    private int gridX;
    private int gridY;

    public ButtonPiece() {

    }

    public ButtonPiece(double radius, double x, double y, Circle circle) {
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.circle = circle;
    }

    public int getGridX() {
        return gridX;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridY(int gridY) {
        this.gridY = gridY;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setColor(Color color){
        circle.setFill(color);
    }

    public void draw(){

        circle.setRadius(this.radius);
        circle.setTranslateX(this.x);
        circle.setTranslateY(this.y);

    }

}

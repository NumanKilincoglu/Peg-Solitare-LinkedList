package com.example.project1;

public class Move {

    public char fromColum;
    public String fromRow;
    public char toColum;
    public String toRow;
    public String direction;

    public Move() {
    }

    public Move(char fromColum, String fromRow, char toColum, String toRow, String direction) {

        this.fromColum = fromColum;
        this.fromRow = fromRow;
        this.toColum = toColum;
        this.toRow = toRow;
        this.direction = direction;

    }

    public String getDirection() {
        return direction;
    }

    public char getFromColum() {
        return fromColum;
    }

    public char getToColum() {
        return toColum;
    }

    public String getFromRow() {
        return fromRow;
    }

    public void setFromRow(String fromRow) {
        this.fromRow = fromRow;
    }

    public String getToRow() {
        return toRow;
    }

    public void setToRow(String toRow) {
        this.toRow = toRow;
    }
}

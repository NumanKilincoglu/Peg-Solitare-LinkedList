package com.example.project1;

import javafx.scene.shape.Rectangle;

public class Node<T> {

    public Node<T> head;
    public Node<T> nextNode;
    public Node<T> downNode;
    public boolean thereIsPeg;
    public int gridX;
    public int gridY;
    public int x;
    public int y;
    public char columName;
    public String rowName;
    public boolean isHeader;
    public Rectangle rec;
    public int centerX;
    public int centerY;
    public ButtonPiece button;

    public Node() {
    }

    public Node(boolean thereIsPeg, int gridX, int gridY, int x, int y, char columName, String rowName, boolean isHeader, int centerX, int centerY) {
        this.thereIsPeg = thereIsPeg;
        this.gridX = gridX;
        this.gridY = gridY;
        this.x = x;
        this.y = y;
        this.columName = columName;
        this.rowName = rowName;
        this.isHeader = isHeader;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public Node(T data) {
        this.nextNode = null;
        this.downNode = null;
    }

    public void addDownNodeLast(T data) {
        Node<T> newChild = new Node<>(data);
        if (head == null) {
            head = newChild;
        } else {
            Node<T> temp = head;
            while (temp.downNode != null) {
                temp = temp.downNode;
            }
            temp.downNode = newChild;
        }
    }

    public void addDownNodeLast(Node<T> node) { // Header nodelara down node ekleme metodu
        if (head == null) {
            head = node;
        } else {
            Node<T> temp = head;

            while (temp.downNode != null) {
                temp = temp.downNode;
            }
            temp.downNode = node;
        }
    }

    public Node<T> getDownNode(char column, String row) { // Satir sutun ismine gore node bulma

        Node<T> temp = head;

        while (temp != null) {

            if (temp.getColumName() == column && temp.getRowName().equals(row)) {

                return temp;
            }

            temp = temp.downNode;
        }

        return null;
    }

    public Node<T> getDownNodeByGridCoordinates(int gridX, int gridY) {//Koordinatlara gore node bulma

        Node<T> temp = head;

        while (temp != null) {

            if (temp.getGridY() ==gridY && temp.getGridX() == gridX) {

                return temp;
            }

            temp = temp.downNode;
        }

        return null;
    }

    public void print() {
        Node<T> temp = head;

        while (temp != null) {

            System.out.println(temp.getColumName() + " -->" + temp.getRowName());

            temp = temp.downNode;
        }
    }

    public boolean isThereIsPeg() {
        return thereIsPeg;
    }

    public void setThereIsPeg(boolean thereIsPeg) {
        this.thereIsPeg = thereIsPeg;
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public char getColumName() {
        return columName;
    }

    public void setColumName(char columName) {
        this.columName = columName;
    }

    public String getRowName() {
        return rowName;
    }

    public void setRowName(String rowName) {
        this.rowName = rowName;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public Rectangle getRec() {
        return rec;
    }

    public void setRec(Rectangle rec) {
        this.rec = rec;
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public ButtonPiece getButton() {
        return button;
    }

    public void setButton(ButtonPiece button) {
        this.button = button;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }
}

package com.example.project1;

public class MultiLinkedList<T> {

    public Node<T> head;

    MultiLinkedList() {
        head = null;
    }

    public void deleteList() {
        head = null;
    }

    public void addFirstNode(Node<T> node) {
        if (head == null) {
            head = node;
        } else {
            node.nextNode = head;
            head = node;
        }

    }

    public void addHeadNodesLastNode(Node<T> node) {
        if (head == null) {
            head = node;
        } else {
            Node<T> temp = head;

            while (temp.nextNode != null) {
                temp = temp.nextNode;
            }
            temp.nextNode = node;
        }
    }

    public Node<T> getNode(char column, String row) { // Satir ve Sutun isimleri araciligiyla node bulma

        Node<T> temp = head;

        while (temp != null) {

            if (temp.getColumName() == column && temp.getRowName().equals(row)) {

                return temp;

            } else {
                if (temp.getDownNode(column, row) != null) {
                    return temp.getDownNode(column, row);
                }

            }

            temp = temp.nextNode;
        }

        return null;
    }

    public Node<T> getNode(int gridX, int gridY) { // Griddeki koordinatlari araciligiyla node bulma

        Node<T> temp = head;
        Node<T> temp2 = null;

        while (temp != null) {

            if (temp.getGridX() == gridX && temp.getGridY() == gridY) {

                return temp;

            } else {
                if (temp.getDownNodeByGridCoordinates(gridX, gridY) != null) {
                    return temp.getDownNodeByGridCoordinates(gridX, gridY);
                }

            }

            temp = temp.nextNode;
        }

        return null;
    }

    public void print() { //Sadece head nodelari yazdirir.(A1 > B1 > C1 > D1 Gibi)
        Node<T> temp = head;

        while (temp != null) {

            System.out.println(temp.columName + "" + temp.rowName);
            temp = temp.nextNode;

        }
    }

    public void print3() { // MultiLinked List icindeki tum nodelarin datalarini yazdirir (next ve down nodelarin)
        Node<T> temp = head;

        while (temp != null) {

            System.out.println(temp.getColumName() + " -->" + temp.getRowName());
            temp.print();
            temp = temp.nextNode;

        }
    }

}

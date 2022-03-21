package com.example.project1;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Optional;

public class gridControllerLinkedList {
    static int totalMoves = 1;
    static int oldX;
    static int oldY;
    static int moveCount = 0;
    private final int gridOffset = 50;
    private final int sqSize = 60;
    public MultiLinkedList<String> gridList = new MultiLinkedList<>(); // Ekrana cizilen Rectangle objelerini tutan MultiLinkedList
    public MultiLinkedList<String> buttonPiecesList = new MultiLinkedList<>();// Ekrana cizilen butonlarin objelerini tutan MultiLinkedList
    public Move moveUp; // Olasi hamleleri tutan Move objeleri
    public Move moveDown;
    public Move moveRight;
    public Move moveLeft;

    int point = 0;
    @FXML
    Pane gridPane;
    int remainingCount = 0;
    private int boardSpots;
    private int size;
    @FXML
    private Label lbl_boardSize;
    @FXML
    private Label lbl_remaining;
    @FXML
    private TextArea lbl_possibleMoves;
    @FXML
    private Label lbl_point;
    @FXML
    private Button btn_start;
    @FXML
    private Button btn_state;
    @FXML
    private Label lbl_gameState;

    public Node<String> initilializeDownNodes(Node<String> head) { // Burada griddeki kareleri tutan MultiLinkedList'in her head nodunun alt nodelarini olsuturuyorum

        Node<String> tmpNode = head;
        int initX = gridOffset;
        int initY = gridOffset + sqSize;

        for (int i = 0; i < boardSpots; i++) {

            for (int j = 0; j < boardSpots - 1; j++) {

                Node<String> downNode = new Node<>();
                Rectangle rec = new Rectangle(initX, (initY + j * sqSize), sqSize, sqSize);
                int cx = (int) rec.getX() + (int) rec.getWidth() / 2;
                int cy = (int) rec.getY() + (int) rec.getHeight() / 2;
                rec.setFill(Color.WHITE);
                rec.setStroke(Color.BLACK);

                gridPane.getChildren().add(rec);
                downNode.setRec(rec);
                downNode.setX((int) rec.getX());
                downNode.setY((int) rec.getY());
                downNode.setCenterX(cx);
                downNode.setCenterY(cy);
                downNode.setGridX(i);
                downNode.setGridY(j + 1);
                downNode.setHeader(false);
                downNode.setThereIsPeg(false);
                downNode.setColumName(tmpNode.getColumName());
                downNode.setRowName(Integer.toString(j + 2));
                tmpNode.addDownNodeLast(downNode); //Down nodelari bribirine bagliyorum
                tmpNode.downNode = downNode;
            }

            initX += sqSize;
            tmpNode = tmpNode.nextNode; // Bir head node'un alk nodelari ayarlaninca diger head node'a geciyorum

        }

        return head;
    }

    public Node<String> getInitialHeadNode() { // Griddeki karelerin head nodunu olsuturuyorum

        Node<String> headNode = new Node<>();
        headNode.setGridX(0);
        headNode.setGridY(0);
        Rectangle rec = new Rectangle(gridOffset, gridOffset, sqSize, sqSize);
        int cx = (int) rec.getX() + (int) rec.getWidth() / 2;
        int cy = (int) rec.getY() + (int) rec.getHeight() / 2;
        rec.setFill(Color.WHITE);
        rec.setStroke(Color.BLACK);
        gridPane.getChildren().add(rec);
        headNode.setRec(rec);
        headNode.setX(gridOffset);
        headNode.setY(gridOffset);
        headNode.setCenterX(cx);
        headNode.setCenterY(cy);
        headNode.isHeader = true;
        headNode.thereIsPeg = false;
        headNode.columName = 'A';
        headNode.rowName = "1";
        gridList.addFirstNode(headNode);
        return headNode;
    }

    public void createHeadNodes() { // Burada, boarddaki kareleri cizmek icin head nodelarimi olusturuyorum

        int tempSqsize = sqSize;

        for (int i = 0; i < boardSpots - 1; i++) {
            Node<String> newNode = new Node<>();

            Rectangle rec = new Rectangle(gridOffset + tempSqsize, gridOffset, sqSize, sqSize);
            int cx = (int) rec.getX() + (int) rec.getWidth() / 2;
            int cy = (int) rec.getY() + (int) rec.getHeight() / 2;

            rec.setFill(Color.WHITE);
            rec.setStroke(Color.BLACK);
            gridPane.getChildren().add(rec);

            newNode.setGridX(i + 1);
            newNode.setGridY(0);
            newNode.setX(gridOffset + tempSqsize);
            newNode.setY(gridOffset);
            newNode.setCenterX(cx);
            newNode.setCenterY(cy);
            newNode.setColumName((char) (66 + i));
            newNode.setRowName("1");
            newNode.setHeader(true);
            newNode.setThereIsPeg(false);
            newNode.setRec(rec);
            tempSqsize += sqSize;
            gridList.addHeadNodesLastNode(newNode);

        }

    }

    public Node<String> createAllButtons() { // Burada buton once butonlarin nodelarini olusturuyorum.

        for (int i = 0; i < boardSpots; i++) { //

            Node<String> tempGrid = gridList.getNode((char) (65 + i), "1"); // Once buttonlarin head node'larini olusturuyorum
            Node<String> buttonHead = new Node<>();
            createHeaderButtons(buttonHead, i, tempGrid);

            for (int j = 0; j < boardSpots - 1; j++) {
                Node<String> tempGridHead = gridList.getNode((char) (65 + i), Integer.toString(j + 2));
                createButtonNodes(buttonHead, tempGridHead);// Head node olusunca onun alt nodelarini olusturuyorum.
            }

            buttonPiecesList.addHeadNodesLastNode(buttonHead); //Asagi dogru olusan listenin head nodunu MultiLinkedList'in sonuna ekliyorum.

        /*

        MultiLinkedList():  A1 -> B1 -> C1   Bu sekilde head nodelar uzerinden tum nodelara erisebiliyorum.
                            A2    B2    C2
                            A3    B3    C3

         */

        }

        return null;
    }

    public void createHeaderButtons(Node<String> buttonHead, int i, Node<String> tempGrid) { // Burada butonlarin header nodelarini olusturuyorum

        Circle circle = new Circle();
        circle.setFill(Color.ORANGE);
        circle.setStroke(Color.BLACK);
        double radius = sqSize / 3.1;
        ButtonPiece p = new ButtonPiece(radius, tempGrid.getCenterX(), tempGrid.getCenterY(), circle); //Circle objesi olusturup bunu buna ekliyorum.
        gridPane.getChildren().add(circle);//Butonu da gridPane'e ekliyorum.
        p.draw();
        p.setGridX(tempGrid.getGridX());
        p.setGridY(tempGrid.getGridY());
        buttonHead.setButton(p);  //Node' un icine de butonu ekliyorum.
        buttonHead.setGridX(i);
        buttonHead.setGridY(0);
        buttonHead.setX(tempGrid.getX());
        buttonHead.setY(tempGrid.getY());
        buttonHead.setCenterX(tempGrid.getCenterX());
        buttonHead.setCenterY(tempGrid.getCenterY());
        buttonHead.setColumName(tempGrid.getColumName());
        buttonHead.setRowName(tempGrid.getRowName());// ilk headerlar 1 olcak a1 b1 gibi
        buttonHead.setHeader(true);
        buttonHead.setThereIsPeg(false);
        circle.setOnMousePressed(event -> mousePressed(event, p));
        circle.setOnMouseDragged(event -> mouseDragged(event, p));
        circle.setOnMouseReleased(event -> mouseReleased(event, p));

    }

    public void createButtonNodes(Node<String> buttonHead, Node<String> tempGridHead) {

        int deleteXGrid = (boardSpots / 2);         // Burda grid olusturulurken eger board size ciftse ortadaki 4 butonu, eger tekse ortadaki 1 butpnu olusturmuyorum
        int deleteYgrid = (boardSpots / 2);

        int cx1, cy1, cx2 = -1, cy2 = -1, cx3, cy3, cx4, cy4;

        if (boardSpots % 2 == 0) {

            cx1 = deleteXGrid;
            cy1 = deleteYgrid;

            cx2 = deleteXGrid - 1;
            cy2 = deleteYgrid;

            cx3 = deleteXGrid - 1;
            cy3 = deleteYgrid - 1;

            cx4 = deleteXGrid;
            cy4 = deleteYgrid - 1;

            int headX = tempGridHead.getGridX();
            int headY = tempGridHead.getGridY();

            if (((cx1 == headX && cy1 == headY) || (cx2 == headX && cy2 == headY) || (cx3 == headX && cy3 == headY) || (cx4 == headX && cy4 == headY))) {

                Node<String> downNode = new Node<>(false, tempGridHead.getGridX(), tempGridHead.getGridY(), tempGridHead.getX(), tempGridHead.getY(), tempGridHead.getColumName(),
                        tempGridHead.getRowName(), false, tempGridHead.getCenterX(), tempGridHead.getCenterY());

                buttonHead.addDownNodeLast(downNode);

            } else {

                Node<String> downNode = new Node<>(true, tempGridHead.getGridX(), tempGridHead.getGridY(), tempGridHead.getX(), tempGridHead.getY(), tempGridHead.getColumName(),
                        tempGridHead.getRowName(), false, tempGridHead.getCenterX(), tempGridHead.getCenterY());

                Circle circle = new Circle();
                circle.setFill(Color.ORANGE);
                circle.setStroke(Color.BLACK);
                double radius = sqSize / 3.1;
                ButtonPiece piece = new ButtonPiece(radius, tempGridHead.getCenterX(), tempGridHead.getCenterY(), circle);
                piece.setGridX(tempGridHead.getGridX());
                piece.setGridY(tempGridHead.getGridY());
                circle.setOnMousePressed(event -> mousePressed(event, piece));
                circle.setOnMouseDragged(event -> mouseDragged(event, piece));
                circle.setOnMouseReleased(event -> mouseReleased(event, piece));
                gridPane.getChildren().add(circle);
                piece.draw();
                downNode.setButton(piece);
                buttonHead.addDownNodeLast(downNode);

            }

        } else {

            cx1 = deleteXGrid;
            cy1 = deleteYgrid;

            int headX = tempGridHead.getGridX();
            int headY = tempGridHead.getGridY();

            if ((cx1 == headX && cy1 == headY) || (cx2 == headX && cy2 == headY)) {

                Node<String> downNode = new Node<>(false, tempGridHead.getGridX(), tempGridHead.getGridY(), tempGridHead.getX(), tempGridHead.getY(),
                        tempGridHead.getColumName(), tempGridHead.getRowName(), false, tempGridHead.getCenterX(), tempGridHead.getCenterY());

                buttonHead.addDownNodeLast(downNode);


            } else {

                Node<String> downNode = new Node<>(true, tempGridHead.getGridX(), tempGridHead.getGridY(), tempGridHead.getX(), tempGridHead.getY(), tempGridHead.getColumName(),
                        tempGridHead.getRowName(), false, tempGridHead.getCenterX(), tempGridHead.getCenterY());

                Circle circle = new Circle();
                circle.setFill(Color.ORANGE);
                circle.setStroke(Color.BLACK);
                double radius = sqSize / 3.1;
                ButtonPiece piece = new ButtonPiece(radius, tempGridHead.getCenterX(), tempGridHead.getCenterY(), circle);
                piece.setGridX(tempGridHead.getGridX());
                piece.setGridY(tempGridHead.getGridY());
                circle.setOnMousePressed(event -> mousePressed(event, piece));
                circle.setOnMouseDragged(event -> mouseDragged(event, piece));
                circle.setOnMouseReleased(event -> mouseReleased(event, piece));
                gridPane.getChildren().add(circle);
                piece.draw();
                downNode.setButton(piece);
                buttonHead.addDownNodeLast(downNode);

            }

        }

    }

    public void textPlacements() { //Grid'in etrafina A1 B2 gibi degerleri yazan metod
        int colNums = 0;
        for (int i = sqSize + gridOffset / 2; i < size + gridOffset; i += sqSize) {
            if (colNums < boardSpots) {
                Text text = new Text(i, 20, Character.toString((char) (65 + colNums)));
                Text text2 = new Text(20, i, Integer.toString(colNums + 1));
                text2.setFill(Color.WHITE);
                text2.setStroke(Color.WHITE);
                text.setFill(Color.WHITE);
                text.setStroke(Color.WHITE);
                gridPane.getChildren().add(text);
                gridPane.getChildren().add(text2);
                colNums++;
            }
        }

    }

    public void checkGameState() {

        if (checkGameBoardMoves()) { //Check Game State butonuna tiklandiginda nodelarin olasi hamleleri hesaplanir. Eger hic olasi hamle yoksa oyun biter varsa devam eder.
            lbl_gameState.setText("Contiune");

        }else{
            lbl_possibleMoves.appendText("GAME OVER! PLEASE RESTART THE GAME!\n");
            gridPane.getChildren().clear();
            lbl_gameState.setText("Game Over");
            gameOverText();

        }
    }

    public void mousePressed(MouseEvent e, ButtonPiece buttonPiece) {

        if (e.getButton() == MouseButton.PRIMARY) {//Butona sol clickle tiklamayi algilar
            int xgrid = ((int) buttonPiece.getX() - gridOffset) / sqSize;// Butona tikladigimizda onun griddeki yerini bul
            int ygrid = ((int) buttonPiece.getY() - gridOffset) / sqSize;

            ButtonPiece piece = buttonPiecesList.getNode(xgrid, ygrid).getButton(); // Koordinatlar ile butonun bulundugu node'u al

            if (piece != null) { // Eger node icinde buton varsa ilk konumunu al ve depola sonrasinda olasi hamlelerini olstur.
                oldX = xgrid;
                oldY = ygrid;
                createPossibleMoves(xgrid, ygrid);
            }

        } else { //Butona sag clickle tiklamayi algilar

            int xgrid = ((int) buttonPiece.getX() - gridOffset) / sqSize;// Butona tikladigimizda onun griddeki yerini bul
            int ygrid = ((int) buttonPiece.getY() - gridOffset) / sqSize;

            ButtonPiece piece = buttonPiecesList.getNode(xgrid, ygrid).getButton(); // Koordinatlar ile butonun bulundugu node'u al

            if (piece != null) { // Eger node icinde buton varsa ilk konumunu al ve depola sonrasinda olasi hamlelerini olstur.
                oldX = xgrid;
                oldY = ygrid;
                createPossibleMoves(xgrid, ygrid);
            }
        }

        buttonPiece.setColor(Color.AQUA);
    }

    public void mouseDragged(MouseEvent e, ButtonPiece buttonPiece) { // Butonu surukluyorum
        buttonPiece.setX(buttonPiece.getX() + e.getX());
        buttonPiece.setY(buttonPiece.getY() + e.getY());
        buttonPiece.draw();
    }

    public void mouseReleased(MouseEvent e, ButtonPiece buttonPiece) { // Butonu biraktigimizda yapilacak islemler

        buttonPiece.setColor(Color.ORANGE);
        int boundX = ((int) buttonPiece.getX());
        int boundY = ((int) buttonPiece.getY());
        ButtonPiece piece = buttonPiecesList.getNode(buttonPiece.getGridX(), buttonPiece.getGridY()).getButton();
        Node<String> node = buttonPiecesList.getNode(buttonPiece.getGridX(), buttonPiece.getGridY());

        boolean boardBoundControl = isMoveOutside(boundX, boundY);

        if (boardBoundControl) { // butonu tasidigimiz yer board disinda ise tasima islemi

            int xgrid = ((int) buttonPiece.getX() - gridOffset) / sqSize;
            int ygrid = ((int) buttonPiece.getY() - gridOffset) / sqSize;
            int cx = gridList.getNode(xgrid, ygrid).getCenterX(); // Hedef node'un griddeki koordinatlarini aliyorum
            int cy = gridList.getNode(xgrid, ygrid).getCenterY();

            if (xgrid == oldX && ygrid == oldY) { // Eger butonu biraktigimiz yer eski yeriyse lokasyonu ayni kalsin
                piece.setX(gridList.getNode(oldX, oldY).getCenterX());
                piece.setY(gridList.getNode(oldX, oldY).getCenterY());
                piece.setGridY(oldY);
                piece.setGridX(oldX);
                piece.draw();
                oldY = -1;
                oldX = -1;
                removePossibleMoves();
                return;
            }

            if (cx != 0 && cy != 0) { // Sectigimiz yer gecerli mi kontrolu
                Node<String> targetButtonNode = buttonPiecesList.getNode(xgrid, ygrid);

                if (isCellEmpty(xgrid, ygrid) && isMoveValid(targetButtonNode.getColumName(), targetButtonNode.getRowName())) { // Eger hamle gecerliyse yapilacak islemler

                    Move move = findDeletedButton(node.getColumName(), node.getRowName(), targetButtonNode.getColumName(), targetButtonNode.getRowName()); // Silinecek butonu bulma
                    deleteButton(move); // Butonu sil
                    piece.setX(cx);
                    piece.setY(cy);
                    piece.setGridY(ygrid); //Yaptigimiz hamle icin butonun yeni yerini ayarlama islemi
                    piece.setGridX(xgrid);
                    node.setButton(null);
                    targetButtonNode.setButton(piece); // Hedef node ' a butonu data olarak ekliyorum
                    targetButtonNode.setThereIsPeg(true);
                    piece.draw();
                    remainingCount--;
                    lbl_remaining.setText(Integer.toString(remainingCount));
                    point += 10;
                    lbl_point.setText(Integer.toString(point));

                } else { // Eger hamle gecersizse butonu eski yerine koy

                    piece.setX(gridList.getNode(oldX, oldY).getCenterX());
                    piece.setY(gridList.getNode(oldX, oldY).getCenterY());
                    piece.setGridY(oldY);
                    piece.setGridX(oldX);
                    piece.draw();
                    oldY = -1;
                    oldX = -1;
                    lbl_possibleMoves.appendText("Invalid Move!\n");

                }
            }

        } else {
            //Hamle grid disindaysa butonu eski yerine koy
            piece.setX(gridList.getNode(oldX, oldY).getCenterX());
            piece.setY(gridList.getNode(oldX, oldY).getCenterY());
            piece.setGridY(oldY);
            piece.setGridX(oldX);
            piece.draw();
            oldY = -1;
            oldX = -1;
            lbl_possibleMoves.appendText("Invalid Move!\n");

        }
        removePossibleMoves();//Butonu biraktigimizda olasi hamleleri sil
    }

    public void deleteButton(Move move) { // Hamle yaptigimizda silinecek olan butonu bulup silen metod

        Node<String> deletedButtonNode;

        if (move.getDirection().equals("UP")) {
            deletedButtonNode = buttonPiecesList.getNode(oldX, oldY - 1);
            gridPane.getChildren().remove(deletedButtonNode.getButton().getCircle());
            deletedButtonNode.setButton(null);
        }

        if (move.getDirection().equals("DOWN")) {
            deletedButtonNode = buttonPiecesList.getNode(oldX, oldY + 1);
            gridPane.getChildren().remove(deletedButtonNode.getButton().getCircle());
            deletedButtonNode.setButton(null);
        }

        if (move.getDirection().equals("RIGHT")) {
            deletedButtonNode = buttonPiecesList.getNode(oldX + 1, oldY);
            gridPane.getChildren().remove(deletedButtonNode.getButton().getCircle());
            deletedButtonNode.setButton(null);
        }

        if (move.getDirection().equals("LEFT")) {
            deletedButtonNode = buttonPiecesList.getNode(oldX - 1, oldY);
            gridPane.getChildren().remove(deletedButtonNode.getButton().getCircle());
            deletedButtonNode.setButton(null);
        }

    }

    public void createPossibleMoves(int xgrid, int ygrid) { // Bu metodda bir butona tıkladiginiz zaman olasi hamlelerin bulunmasi islemi gerceklestiriliyor

        if (buttonPiecesList.getNode(xgrid, ygrid - 1) != null && buttonPiecesList.getNode(xgrid, ygrid - 1).getButton() != null) { // Yukari hamle kontrolu
            if (isThereNode(xgrid, ygrid - 2)) {
                if (isCellEmpty(xgrid, ygrid - 2)) { // Eger bir ustteki node'da buton varsa ve bir ustundeki node'da buton yoksa olasi hamle olarak alabiliriz
                    Node<String> tmptoNode = buttonPiecesList.getNode(xgrid, ygrid - 2);
                    Node<String> tmpFromNode = buttonPiecesList.getNode(xgrid, ygrid);
                    Move move = new Move(tmpFromNode.getColumName(), tmpFromNode.getRowName(), tmptoNode.getColumName(), tmptoNode.getRowName(), "UP"); // Hamleyi Move class objesi yaratarak olsuturuyorum.
                    if (gridList.getNode(xgrid, ygrid - 2).getRec() != null) { // Burada ise, olasi hamle mevcutsa dikdortgeni yesil renge boyuyorum
                        gridList.getNode(xgrid, ygrid - 2).rec.setFill(Color.LIGHTGREEN);
                    }
                    moveUp = move;
                }
            }
        }

        if (buttonPiecesList.getNode(xgrid, ygrid + 1) != null && buttonPiecesList.getNode(xgrid, ygrid + 1).getButton() != null) { // Asagi hamle kontrolu
            if (isThereNode(xgrid, ygrid + 2)) {
                if (isCellEmpty(xgrid, ygrid + 2)) {
                    Node<String> tmptoNode = buttonPiecesList.getNode(xgrid, ygrid + 2);
                    Node<String> tmpFromNode = buttonPiecesList.getNode(xgrid, ygrid);
                    Move move = new Move(tmpFromNode.getColumName(), tmpFromNode.getRowName(), tmptoNode.getColumName(), tmptoNode.getRowName(), "DOWN");
                    if (gridList.getNode(xgrid, ygrid + 2).getRec() != null) {
                        gridList.getNode(xgrid, ygrid + 2).rec.setFill(Color.LIGHTGREEN);
                    }
                    moveDown = move;
                }
            }
        }

        if (buttonPiecesList.getNode(xgrid + 1, ygrid) != null && buttonPiecesList.getNode(xgrid + 1, ygrid).getButton() != null) { // Sag hamle kontrolu
            if (isThereNode(xgrid + 2, ygrid)) {
                if (isCellEmpty(xgrid + 2, ygrid)) {
                    Node<String> tmptoNode = buttonPiecesList.getNode(xgrid + 2, ygrid);
                    Node<String> tmpFromNode = buttonPiecesList.getNode(xgrid, ygrid);
                    Move move = new Move(tmpFromNode.getColumName(), tmpFromNode.getRowName(), tmptoNode.getColumName(), tmptoNode.getRowName(), "RIGHT");
                    if (gridList.getNode(xgrid + 2, ygrid).getRec() != null) {
                        gridList.getNode(xgrid + 2, ygrid).getRec().setFill(Color.LIGHTGREEN);
                    }
                    moveRight = move;
                }
            }
        }

        if (buttonPiecesList.getNode(xgrid - 1, ygrid) != null && buttonPiecesList.getNode(xgrid - 1, ygrid).getButton() != null) { // Sol hamle kontrolu
            if (isThereNode(xgrid - 2, ygrid)) {
                if (isCellEmpty(xgrid - 2, ygrid)) {
                    Node<String> tmptoNode = buttonPiecesList.getNode(xgrid - 2, ygrid);
                    Node<String> tmpFromNode = buttonPiecesList.getNode(xgrid, ygrid);
                    Move move = new Move(tmpFromNode.getColumName(), tmpFromNode.getRowName(), tmptoNode.getColumName(), tmptoNode.getRowName(), "LEFT");
                    if (gridList.getNode(xgrid - 2, ygrid).getRec() != null) {
                        gridList.getNode(xgrid - 2, ygrid).getRec().setFill(Color.LIGHTGREEN);
                    }
                    moveLeft = move;
                }
            }
        }
        showPossibleMoves();
    }

    public boolean checkGameOverState(int xgrid, int ygrid) { // Bu metodda bir butona tıkladiginiz zaman olasi hamlelerin bulunmasi islemi gerceklestiriliyor
        boolean rvalue = false;
        if (buttonPiecesList.getNode(xgrid, ygrid - 1) != null && buttonPiecesList.getNode(xgrid, ygrid - 1).getButton() != null) { // Yukari hamle kontrolu
            if (isThereNode(xgrid, ygrid - 2)) {
                if (isCellEmpty(xgrid, ygrid - 2)) { // Eger bir ustteki node'da buton varsa ve bir ustundeki node'da buton yoksa olasi hamle olarak alabiliriz
                    rvalue = true;
                }
            }
        }

        if (buttonPiecesList.getNode(xgrid, ygrid + 1) != null && buttonPiecesList.getNode(xgrid, ygrid + 1).getButton() != null) { // Asagi hamle kontrolu
            if (isThereNode(xgrid, ygrid + 2)) {
                if (isCellEmpty(xgrid, ygrid + 2)) {
                    rvalue = true;
                }
            }
        }

        if (buttonPiecesList.getNode(xgrid + 1, ygrid) != null && buttonPiecesList.getNode(xgrid + 1, ygrid).getButton() != null) { // Sag hamle kontrolu
            if (isThereNode(xgrid + 2, ygrid)) {
                if (isCellEmpty(xgrid + 2, ygrid)) {
                    rvalue = true;
                }
            }
        }

        if (buttonPiecesList.getNode(xgrid - 1, ygrid) != null && buttonPiecesList.getNode(xgrid - 1, ygrid).getButton() != null) { // Sol hamle kontrolu
            if (isThereNode(xgrid - 2, ygrid)) {
                if (isCellEmpty(xgrid - 2, ygrid)) {
                    rvalue = true;
                }
            }
        }
        return rvalue;
    }

    public void showPossibleMoves() { // Bu metodda uzerine tikladıgımız butonun olasi hamlelerini GUI uzerindeki text area'ya yazdiriyorum

        String message = "";
        moveCount = 0;
        if (moveUp != null) {
            message = totalMoves++ + ". Choosen Location: " + moveUp.getFromColum() + "" + moveUp.getFromRow() +
                    "\nPossible Hit location: " + moveUp.getToColum() + "" + moveUp.getToRow() + "\n";
            lbl_possibleMoves.appendText(message);
            moveCount++;
        }
        if (moveDown != null) {
            message = totalMoves++ + ". Choosen Location: " + moveDown.getFromColum() + "" + moveDown.getFromRow() +
                    "\nPossible Hit location: " + moveDown.getToColum() + "" + moveDown.getToRow() + "\n";
            lbl_possibleMoves.appendText(message);
            moveCount++;
        }
        if (moveLeft != null) {
            message = totalMoves++ + ". Choosen Location: " + moveLeft.getFromColum() + "" + moveLeft.getFromRow() +
                    "\nPossible Hit location: " + moveLeft.getToColum() + "" + moveLeft.getToRow() + "\n";
            lbl_possibleMoves.appendText(message);
            moveCount++;
        }
        if (moveRight != null) {
            message = totalMoves++ + ". Choosen Location: " + moveRight.getFromColum() + "" + moveRight.getFromRow() +
                    "\nPossible Hit location: " + moveRight.getToColum() + "" + moveRight.getToRow() + "\n";
            lbl_possibleMoves.appendText(message);
            moveCount++;
        }
        if (moveCount == 0) {
            lbl_possibleMoves.appendText("There is No possible move(s)!\n");
        }
    }

    public void removePossibleMoves() { // Sectigimiz butonu released konumuna aldigimizda bu butonun olasi hamlelerini siliyorum

        if (moveUp != null) {
            changeRectangleColor(moveUp);
        }
        if (moveDown != null) {
            changeRectangleColor(moveDown);
        }
        if (moveLeft != null) {
            changeRectangleColor(moveLeft);
        }
        if (moveRight != null) {
            changeRectangleColor(moveRight);
        }
        moveUp = null;
        moveDown = null;
        moveRight = null;
        moveLeft = null;
    }

    public void changeRectangleColor(Move move) { // Hamle yapildiginda griddeki yesil kareleri beyaz yapiyorum
        if (gridList.getNode(move.getToColum(), move.getToRow()) != null) {
            if (gridList.getNode(move.getToColum(), move.getToRow()).getRec() != null) {
                gridList.getNode(move.getToColum(), move.getToRow()).getRec().setFill(Color.WHITE);
            }
        }
    }

    public boolean isMoveOutside(int boundX, int boundY) { // Yapmak istedigimiz hamlenin board'un disinda mi icinde mi oldugunun kontrolu
        return ((boundX > gridOffset) && (boundX < (size + gridOffset)) && (boundY > gridOffset) && (boundY < (size + gridOffset)));
    }

    public boolean isThereNode(int gridX, int gridY) { // Node mevcut mu kontrolu
        return buttonPiecesList.getNode(gridX, gridY) != null;
    }

    public boolean isCellEmpty(int gridX, int gridY) { // Hedef konum bos mu kontrolu
        return buttonPiecesList.getNode(gridX, gridY).getButton() == null;
    }

    public boolean checkGameBoardMoves() {

        for (int i = 0; i < boardSpots; i++) {
            for (int j = 0; j < boardSpots - 1; j++) {
                ButtonPiece piece = buttonPiecesList.getNode(i, j).getButton();
                if (piece != null && checkGameOverState(i, j)) {
                    removePossibleMoves();
                    return true;
                }
            }
        }
        removePossibleMoves();
        return false;
    }

    public boolean isMoveValid(char column, String row) { //Hedef konum olasi hameleler listesinde var mi kontrol. Eger varsa hamle gecerli yoksa gecersiz

        if (moveUp != null && moveUp.getToColum() == column && moveUp.getToRow().equals(row)) {
            return true;
        }
        if (moveDown != null && moveDown.getToColum() == column && moveDown.getToRow().equals(row)) {
            return true;
        }
        if (moveLeft != null && moveLeft.getToColum() == column && moveLeft.getToRow().equals(row)) {
            return true;
        }
        if (moveRight != null && moveRight.getToColum() == column && moveRight.getToRow().equals(row)) {
            return true;
        }

        return false;

    }

    public Move findDeletedButton(char column, String row, char toColumn, String toRow) { // Hamle yapildiktan sonra silinecek butonu bulan metod

        if (moveUp != null && moveUp.getToColum() == toColumn && moveUp.getToRow().equals(toRow) && moveUp.getFromColum() == column && moveUp.getFromRow().equals(row)) {
            return moveUp;
        }
        if ((moveDown != null) && (moveDown.getToColum() == toColumn) && (moveDown.getToRow().equals(toRow)) && (moveDown.getFromColum() == column) && (moveDown.getFromRow().equals(row))) {
            return moveDown;
        }
        if ((moveLeft != null) && (moveLeft.getToColum() == toColumn) && (moveLeft.getToRow().equals(toRow)) && (moveLeft.getFromColum() == column) && (moveLeft.getFromRow().equals(row))) {
            return moveLeft;
        }
        if ((moveRight != null) && (moveRight.getToColum() == toColumn) && (moveRight.getToRow().equals(toRow)) && (moveRight.getFromColum() == column) && (moveRight.getFromRow().equals(row))) {
            return moveRight;
        }

        return null;

    }

    public boolean setSize() { // Start butonuna tikladigimizda board size'ini ayaralyan metod

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Choose Board Size!");
        dialog.setHeaderText("Choose Board Size!");
        dialog.setContentText("Please Choose Board Size!");
        Optional<String> boardSize = dialog.showAndWait();

        if (boardSize.isPresent()) {
            if (boardSize.get().matches("[0-9]+") && Integer.parseInt(boardSize.get()) > 4) {
                boardSpots = Integer.parseInt(boardSize.get());
                lbl_boardSize.setText(boardSize.get() + " X " + boardSize.get());
                return true;
            }
        }
        return false;
    }

    public void startGame() { // Oyunu baslattigimizda yapilan ayarlar

        while (!setSize()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Please enter only digit.");
            alert.setContentText("Enter Number!(Number must greater than 4!)");
            alert.showAndWait();
        }

        gridPane.getChildren().clear();
        size = boardSpots * sqSize;
        moveUp = null;
        moveDown = null;
        moveLeft = null;
        moveRight = null;
        point = 0;

        if (boardSpots % 2 == 0) {
            remainingCount = boardSpots * boardSpots - 4;
        } else {
            remainingCount = boardSpots * boardSpots - 1;
        }

        lbl_possibleMoves.clear();
        moveCount = 0;
        lbl_gameState.setText("Contiune");
        lbl_remaining.setText(Integer.toString(remainingCount));
        lbl_point.setText(Integer.toString(point));
        gridList.deleteList();
        buttonPiecesList.deleteList();
        textPlacements();
        Node<String> gridHead = getInitialHeadNode();
        createHeadNodes();
        initilializeDownNodes(gridHead);
        createAllButtons();

    }

    public void beforeStart() { // Oyun baslamadan once ekrana bilgi mesaji yazdiriyorum

        Text text = new Text(70, 260, "Please Click The Start Button And Enter Grid Size!\n\t\t\t  Then Game Will Start");
        text.setFill(Color.WHITE);
        text.setStroke(Color.WHITE);
        text.setStyle("-fx-font: 24 calibri;");
        text.setTextAlignment(TextAlignment.LEFT);
        gridPane.getChildren().add(text);

    }

    public void gameOverText() { // Oyun baslamadan once ekrana bilgi mesaji yazdiriyorum

        Text text = new Text(70, 250, "\t\t\t\t Game Over!\nPlease Click The Start Button And Enter Grid Size!\n\t\t\t  Then Game Will Start");
        text.setFill(Color.WHITE);
        text.setStroke(Color.WHITE);
        text.setStyle("-fx-font: 24 calibri;");
        text.setTextAlignment(TextAlignment.LEFT);
        gridPane.getChildren().add(text);

    }

    @FXML
    public void initialize() {
        beforeStart();
    }
}

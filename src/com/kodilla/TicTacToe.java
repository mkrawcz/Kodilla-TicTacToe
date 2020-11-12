package com.kodilla;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TicTacToe extends Application {

    public static final Image EMPTY = new Image("file:src/resources/empty.png");
    public static final Image X_SIGN = new Image("file:src/resources/x-sign-s.png");
    public static final Image O_SIGN = new Image("file:src/resources/o-sign-s.png");
    private static final String EASY_MODE = "Easy";
    private static final String HARD_MODE = "Hard";

    private Image imageback = new Image("file:src/resources/check-in-grid.png");
    //GridPane odpowiedzialny ze prezentacje planszy
    private GridPane board = new GridPane();
    //tablica reprezentujaca uklad na planszy
    private Field[][] boardFields = new Field[3][3];

    private Label lbResult;

    private int playerScore = 0;
    private int cpuScore = 0;

    private File savedState = new File("saved.state");
    private File savedRanking = new File("saved.ranking");

    ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
            EASY_MODE, HARD_MODE)
    );

    private List<String> ranking;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //Przygotowanie tła
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(imageback, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        //Stworznie głownego BorderPane
        BorderPane mainBorderPane = new BorderPane();
        //Ustawienie tła w głownym BorderPane
        mainBorderPane.setBackground(background);

        //ustawienie GridPane odpowiedzialnego za prezentacje planszy
        board.setPadding(new Insets(90, 0, 0, 260));
        board.setHgap(50);
        board.setVgap(40);
        board.setTranslateX(12);
        board.setTranslateY(10);
        board.setGridLinesVisible(true);
        //wlozenie GridPane planszy w srodek glownego BorderPane
        mainBorderPane.setCenter(board);

        //todo Stworzyc przyciski / labele (np z wynikiem itd.) i dodać do HBox
        HBox buttonsHbox = createTopMenuHbox();

        lbResult = new Label();
        buttonsHbox.getChildren().add(lbResult);
        refreshScoreLabel();

        ranking = loadRanking();

        mainBorderPane.setTop(buttonsHbox);


        //stworzenie pol na planszy
        createBoardFields();

        //uzupelnienie GridPane planszy na podstawie tablicy reprezentujacej uklad planszy
        fillBoard();

        //utworzenie glownej sceny
        Scene scene = new Scene(mainBorderPane, 1024, 768, Color.BLACK);

        primaryStage.setTitle("TIC TAC TOE");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void fillBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board.add(boardFields[i][j], i, j, 1, 1);
            }
        }
    }

    private void createBoardFields() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardFields[i][j] = createBoardField();
            }
        }
    }

    private Field createBoardField() {
        Field field = new Field(EMPTY);

        field.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("Click...");
            if (field.getImage().equals(EMPTY)) {
                field.setImage(O_SIGN);
                boolean isGameOver = checkGameFinished();
                if (!isGameOver) {
                    cpuTurn();
                    checkGameFinished();
                }
            }
        });

        return field;
    }

    private void showGameOverAlert(String winner) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        if (winner.equals("draw")) {
            alert.setHeaderText("Remis!");
        } else {
            alert.setHeaderText("Wygrał " + winner + "!");
        }
        alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
            @Override
            public void handle(DialogEvent event) {
                resetBoard();
            }
        });

        alert.setContentText("Gra została zakończona");

        alert.show();
    }

    public HBox createTopMenuHbox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");

        Button buttonLoad = new Button("Load");
        buttonLoad.setPrefSize(100, 20);
        buttonLoad.setOnAction(e -> {
            loadGame();
        });

        Button buttonSave = new Button("Save");
        buttonSave.setPrefSize(100, 20);
        buttonSave.setOnAction(e -> {
            saveGame();
        });

        Button buttonReset = new Button("Reset");
        buttonReset.setPrefSize(100, 20);
        buttonReset.setOnAction(e -> {
            resetBoard();
        });

        Button buttonSaveResult = new Button("Save result");
        buttonSaveResult.setPrefSize(100, 20);
        buttonSaveResult.setOnAction(e -> {
            saveRanking();
        });

        Button buttonShowRanking = new Button("Results");
        buttonShowRanking.setPrefSize(100, 20);
        buttonShowRanking.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setHeaderText("Ranking");

            StringBuilder stringBuilder = new StringBuilder();

            for(String el : ranking) {
                stringBuilder.append(el);
                stringBuilder.append("\n");
            }
            alert.setContentText(stringBuilder.toString());

            alert.show();
        });

        cb.setValue(EASY_MODE);

        hbox.getChildren().addAll(buttonLoad, buttonSave, buttonReset, buttonSaveResult, buttonShowRanking, cb);

        return hbox;
    }

    //todo Metoda, ktora sprawdzi czy gra sie zakonczyla
    private boolean checkGameFinished() {
        if (checkWin(O_SIGN)) {
            showGameOverAlert("gracz");
            playerScore++;
            refreshScoreLabel();
            return true;
        } else if (checkWin(X_SIGN)) {
            showGameOverAlert("CPU");
            cpuScore++;
            refreshScoreLabel();
            return true;
        }

        return checkDraw();
    }

    private boolean checkDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (boardFields[i][j].getImage().equals(EMPTY)) {
                    return false;
                }
            }
        }
        showGameOverAlert("draw");
        return true;
    }

    private boolean checkWin(Image image) {
        for (int i = 0; i < 3; i++) {
            //poziomo
            if (boardFields[i][0].getImage().equals(image)
                    && boardFields[i][1].getImage().equals(image)
                    && boardFields[i][2].getImage().equals(image)) {
                return true;
            }

            //pionowa
            if (boardFields[0][i].getImage().equals(image)
                    && boardFields[1][i].getImage().equals(image)
                    && boardFields[2][i].getImage().equals(image)) {
                return true;
            }
            //przekatne
            if (boardFields[0][0].getImage().equals(image)
                    && boardFields[1][1].getImage().equals(image)
                    && boardFields[2][2].getImage().equals(image)) {
                return true;
            }
            if (boardFields[0][2].getImage().equals(image)
                    && boardFields[1][1].getImage().equals(image)
                    && boardFields[2][0].getImage().equals(image)) {
                return true;
            }

        }
        return false;
    }

    //todo Metoda, ktora wykona ruch komputera
    private void cpuTurn() {
        System.out.println("Ruch przeciwnika");

        String mode = (String) cb.getValue();
        if (mode.equals(EASY_MODE)) {
            cpuEasyTurn();
        } else if (mode.equals(HARD_MODE)) {
            cpuHardTurn();
        }

    }

    public void cpuEasyTurn() {
        Random random = new Random();

        boolean success = false;

        while (!success) {
            int x = random.nextInt(3);
            int y = random.nextInt(3);

            if (boardFields[x][y].getImage().equals(EMPTY)) {
                boardFields[x][y].setImage(X_SIGN);
                success = true;
            }
        }
    }

    //troche trudniej byc powinno
    public void cpuHardTurn() {
        if (boardFields[0][0].getImage().equals(X_SIGN) && boardFields[0][1].getImage().equals(X_SIGN)
            && boardFields[0][2].getImage().equals(EMPTY)) {
            boardFields[0][2].setImage(X_SIGN);
        } else if (boardFields[0][0].getImage().equals(X_SIGN) && boardFields[1][0].getImage().equals(X_SIGN)
                && boardFields[2][0].getImage().equals(EMPTY)) {
            boardFields[2][0].setImage(X_SIGN);
        } else if (boardFields[0][0].getImage().equals(X_SIGN) && boardFields[1][1].getImage().equals(X_SIGN)
                && boardFields[2][2].getImage().equals(EMPTY)) {
            boardFields[2][2].setImage(X_SIGN);
        } else if (boardFields[0][2].getImage().equals(X_SIGN) && boardFields[1][1].getImage().equals(X_SIGN)
                && boardFields[2][0].getImage().equals(EMPTY)) {
            boardFields[2][0].setImage(X_SIGN);
        } else{
            cpuEasyTurn();
        }

    }

    private void saveRanking() {
        String actualResult = "Gracz " + playerScore + " : " + cpuScore + " " + "CPU";
        ranking.add(actualResult);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(savedRanking));
            oos.writeObject(ranking);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> loadRanking() {
        List<String> result = new ArrayList<>();
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(savedRanking));
            Object readObject = ois.readObject();
            if (readObject instanceof List) {
                result = (List<String>) readObject;
            }
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    private void saveGame() {
        GameState gameState = new GameState(boardFields, playerScore, cpuScore);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(savedState));
            oos.writeObject(gameState);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadGame() {
        GameState gameState = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(savedState));
            Object readObject = ois.readObject();
            if (readObject instanceof GameState) {
                gameState = (GameState) readObject;
            }
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        playerScore = gameState.getPlayerScore();
        cpuScore = gameState.getCpuScore();
        refreshScoreLabel();

        Field[][] retrievedBoard = gameState.getBoardFields();

        for (int i = 0; i < boardFields.length; i++) {
            for (int j = 0; j < boardFields[i].length; j++) {
                boardFields[i][j].setImage(retrievedBoard[i][j].getImage());
            }
        }
    }

    private void resetBoard() {
        createBoardFields();
        fillBoard();
    }

    //Metoda odświeża label prezentujący wynik
    private void refreshScoreLabel() {
        String score = createScoreString();
        lbResult.setText(score);
    }

    private String createScoreString() {
        return playerScore + " : " + cpuScore;
    }

}

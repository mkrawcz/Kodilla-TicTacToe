package com.kodilla;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class TicTacToe extends Application {

    public static final Image EMPTY = new Image("file:src/resources/empty.png");
    public static final Image X_SIGN = new Image("file:src/resources/x-sign-s.png");
    public static final Image O_SIGN = new Image("file:src/resources/o-sign-s.png");

    private Image imageback = new Image("file:src/resources/check-in-grid.png");
    //GridPane odpowiedzialny ze prezentacje planszy
    private GridPane board = new GridPane();
    //tablica reprezentujaca uklad na planszy
    private Field[][] boardFields = new Field[3][3];

    private Label lbResult;

    private int playerScore = 0;
    private int cpuScore = 0;

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
                //todo Sprawdz czy gra zostala zakonczona wygrana lub remisem
                //todo Jesli nie, to nastepuje ruch komputera i znowu sprawdzamy czy gra sie skonczyla
            }
        });

        return field;
    }

    public HBox createTopMenuHbox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");

        //todo do przyciskow dodac akcje czyli np wywolanie metod https://docs.oracle.com/javafx/2/ui_controls/button.htm example 3.3

        Button buttonPlay = new Button("Play");
        buttonPlay.setPrefSize(100, 20);

        Button buttonSave = new Button("Save");
        buttonSave.setPrefSize(100, 20);

        //todo dodać przycisk reset
        Button buttonReset = new Button("Reset");
        buttonReset.setPrefSize(100, 20);


        buttonSave.setOnAction(e -> {
            //todo wywołaj metodę zapisująca
        });

        hbox.getChildren().addAll(buttonPlay, buttonSave, buttonReset);

        return hbox;
    }

    //todo Metoda, ktora sprawdzi czy gra sie zakonczyla

    //todo Metoda, ktora wykona ruch komputera

    //todo Metoda, ktora zresetuje stan planszy
    private void resetBoard

    //Metoda odświeża label prezentujący wynik
    private void refreshScoreLabel() {
        String score = createScoreString();
        lbResult.setText(score);
    }

    //todo na podstawie zmiennych playerScore i cpuScore zwróć wynik w postaci String
    private String createScoreString() {
        return playerScore + " : " + cpuScore;
    }

}

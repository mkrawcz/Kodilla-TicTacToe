package com.kodilla;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Group;


import java.security.Principal;
import java.util.Enumeration;

public class TicTacToe extends Application {

    private Image imageback = new Image("file:src/resources/check-in-grid.png");
    private GridPane board = new GridPane();

    public static void main(String[] args) {
        launch(args);
    }
    //ClassLoader classLoader = getClass().getClassLoader();
            //new Image(classLoader.getResource("resources/check-in-grid.png"));

    @Override
    public void start(Stage primaryStage) throws Exception {
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(imageback, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.setBackground(background);

        board.setPadding(new Insets(50, 0, 0, 50));
        board.setHgap(12);
        board.setVgap(5);
        board.setTranslateX(12);
        board.setTranslateY(10);
        board.setGridLinesVisible(true);
        mainBorderPane.setCenter(board);

        //dodac 9 ImageView np. z X tak aby sie pokazaly i dostosowac board do tla
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board.add(new ImageView(new Image("file:src/resources/x-sign.png")), i, j, 1, 1);
            }
        }

        Scene scene = new Scene(mainBorderPane, 1024, 768, Color.BLACK);

        primaryStage.setTitle("TIC TAC TOE");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }
}

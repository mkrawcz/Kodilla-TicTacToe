package com.kodilla;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Group;


import java.awt.*;
import java.security.Principal;
import java.util.Enumeration;

public class TicTacToe extends Application {




    public static void main(String[] args) {
        launch(args);
    }

    ClassLoader classLoader = getClass().getClassLoader();
    private Image imageback = new Image(classLoader.getResource("resources/check-in-grid.png"));

    @Override
    public void start(Stage primaryStage) throws Exception {
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(imageback, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        GridPane grid = new GridPane();
        grid.setBackground(background);

        Scene scene = new Scene(grid, 300, 300, Color.BLACK);

        primaryStage.setTitle("TIC TAC TOE");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}

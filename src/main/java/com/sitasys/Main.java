package com.sitasys;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application{
    public static void main(String[] args){
        launch(args);
    }

    /**
     * Start of Program
     * @param primaryStage Stage
     * @throws Exception Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(getGUI(),1000,700));
        primaryStage.setTitle("MandelBrot- und Juliamengen berechner");
        primaryStage.show();
    }
    private BorderPane getGUI(){
        return new MandelBrotGUI();
    }
}

package com.example.frontproject3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class bot extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("clientlogin.fxml"));
        primaryStage.setScene(new Scene(fxmlLoader.load(), 600, 480));
        primaryStage.setMinHeight(480);
        primaryStage.setMinWidth(540);
        primaryStage.show();
    }
}

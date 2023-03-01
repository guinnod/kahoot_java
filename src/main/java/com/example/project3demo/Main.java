package com.example.project3demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.SplittableRandom;
import java.util.Stack;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    public static MediaPlayer mediaPlayer;
    @FXML
    StackPane stackPane;
    @Override
    public void start(Stage primaryStage) throws IOException {
        Image icon = new Image(new File("resources\\img\\kk.png").toURI().toString());
        primaryStage.getIcons().add(icon);
        StackPane pane = new StackPane();
        pane.setStyle("-fx-background-image: url('file:/C:/Users/Абзал/Desktop/resources/img/background.jpg'); " +
                "-fx-background-size: stretch;" +
                "-fx-background-position: center;" +
                "-fx-background-repeat: no-repeat;");
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("clientwaiting.fxml"));
        BorderPane borderPane = loader.load();
        Button button = new Button("Choose a file");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Quiz Source Files", "*.txt"));
        button.setOnAction(actionEvent -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            try {
                Quiz quiz = new Quiz();
                quiz.makeQuiz(file);
                ArrayList<Question> questions = quiz.getQuestions();
                int pin = 100000 + (int)(Math.random()*899999);
                int port = pin/100;
                FlowPane flowPane = (FlowPane)borderPane.lookup("#flowPane");
                Label count = (Label)borderPane.lookup("#count");

                Server server = new Server(port, questions, flowPane, count, primaryStage);
                Label label = (Label) borderPane.lookup("#label");
                label.setText(String.valueOf(pin));
                Button button1 = (Button)borderPane.lookup("#button");
                button1.setOnAction(actionEvent1 -> {
                    try {
                        server.stopAccepting();
                        server.trying(0);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                server.turnON();
                Platform.runLater(() -> {
                    primaryStage.setScene(new Scene(borderPane, 600, 480));
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        mediaPlayer = new MediaPlayer(new Media(new File("./src/main/resources/kahoot_music.mp3").toURI().toString()));
        mediaPlayer.play();
        pane.getChildren().add(button);
        Scene scene = new Scene(pane, 600, 480);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Project 2");
        primaryStage.show();
    }
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        int n = nums1.length;
        int[] nums = new int[n];
        int[] numsTwo = new int[nums2.length];
        Stack<Integer> stack = new Stack<>();
        for (int i = nums2.length-1; i > -1; i--) {
            if (stack.isEmpty()) {
                numsTwo[i] = -1;
            }
            else {
                while (!stack.isEmpty() && stack.peek() >= nums2[i]) {
                    stack.pop();
                }
                if (stack.isEmpty()) {
                    numsTwo[i] = -1;
                }
                else {
                    numsTwo[i] = stack.peek();
                }
            }
            stack.push(nums2[i]);
        }

        return nums;
    }
}

/*
*
* 6 1 3 7 4 2
* 2
* 4
* 7
* 3 7
* 1 3 7
* 6 7
*
*
*
* */

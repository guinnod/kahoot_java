package com.example.frontproject3;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class clientwaitingctrl implements Runnable{
    @FXML
    Circle circle;
    @FXML
    Rectangle rectangle;
    @FXML
    BorderPane borderPane;
    @FXML
    Label label;
    @FXML
    Label count;
    @FXML
    Button button;
    @FXML
    FlowPane flowPane;

    public void buttonAction(){
        int i = Integer.parseInt(count.getText());
        i++;
        count.setText(String.valueOf(i));
        String[] text = {"bot", "client", "Person", "NickName", "dvsfadc"};
        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: #0000001a");
        Label label1 = new Label(text[(int)(Math.random()*4)]);
        label1.setFont(new Font("Arial Bold", 18));
        label1.setStyle("-fx-text-fill: #f2f2f2");
        stackPane.getChildren().add(label1);
        stackPane.setPadding(new Insets(10));
        FlowPane.setMargin(stackPane, new Insets(10));
        flowPane.getChildren().add(stackPane);
    }
    @Override
    public void run() {
        try {
            label.setText(String.valueOf(100000 + ((int)(Math.random()*899999))));
        }
        catch (Exception e){
            while (true) {
                long s =  System.currentTimeMillis();
                if (System.currentTimeMillis()-s>1000){
                    System.out.println("ex");

                    break;
                }
            }
            run();
        }

    }
}

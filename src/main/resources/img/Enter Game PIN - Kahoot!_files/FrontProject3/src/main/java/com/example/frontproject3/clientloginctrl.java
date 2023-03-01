package com.example.frontproject3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class clientloginctrl {
    @FXML
    private TextField textField;
    @FXML
    private Button enter;
    @FXML
    public void sendPin() {
        try {
            int pin = Integer.parseInt(textField.getText());
            pin = pin/10;
            textField.setStyle("-fx-text-fill: #333333");
        }
        catch (Exception e) {
            textField.setStyle("-fx-text-fill: red");
            textField.setText("Wrong PIN!");
        }

    }
}

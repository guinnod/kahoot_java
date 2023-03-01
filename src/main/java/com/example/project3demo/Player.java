package com.example.project3demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Player extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private boolean current;
    private int currpoint;
    private int point;
    //int point;
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("clientlogin.fxml"));
        BorderPane borderPane = loader.load();
        Button button = (Button)borderPane.lookup("#enter");
        TextField textField = (TextField)borderPane.lookup("#textField");
        button.setOnAction(actionEvent -> {
            try {
                textField.setStyle("-fx-text-fill: #333333");
                int pin = Integer.parseInt(textField.getText());
                pin = pin/100;
                this.socket = new Socket("localhost", pin);
                textField.replaceText(0, textField.getText().length(), "");
                textField.setPromptText("Nickname");
                button.setOnAction(actionEvent1 -> {
                    try {
                        this.outputStream = new DataOutputStream(socket.getOutputStream());
                        outputStream.writeUTF(textField.getText());
                        Platform.runLater(() -> {
                            try {
                                Pane pane = new FXMLLoader(HelloApplication.class.getResource("waitingforclients.fxml")).load();
                                new Thread(() -> {
                                    try {
                                        playGame(socket, primaryStage);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }).start();

                                Platform.runLater(() -> {
                                    primaryStage.setScene(new Scene(pane, 600, 480));
                                });

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            catch (Exception e) {
                textField.setStyle("-fx-text-fill: red");
                textField.setText("Wrong PIN!");
            }
        });
        primaryStage.setScene(new Scene(borderPane, 600, 480));
        primaryStage.show();
    }
    public void playGame(Socket socket, Stage stage) throws IOException {
        this.inputStream = new DataInputStream(socket.getInputStream());
        //AtomicInteger point = new AtomicInteger();
        while (true) {
            AtomicBoolean b = new AtomicBoolean(true);
            String command = inputStream.readUTF();
            String answer = inputStream.readUTF();

            long starttime = System.currentTimeMillis();
            if (command.equals("stop")) {
                outputStream.writeInt(point);
                break;
            }
            else if (command.equals("skipped")) {
                Platform.runLater(() -> {
                    BorderPane borderPane;
                    if (current) {
                        try {
                            borderPane = new FXMLLoader(HelloApplication.class.getResource("correct.fxml")).load();
                            Label label = (Label)borderPane.lookup("#correctp");
                            label.setText("+" + currpoint);
                            System.out.println(currpoint);
                            stage.setScene(new Scene(borderPane, 600, 480));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    else {
                        try {
                            borderPane = new FXMLLoader(HelloApplication.class.getResource("incorrect.fxml")).load();
                            stage.setScene(new Scene(borderPane, 600, 480));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                });

            }
            else if (command.equals("Test")) {
                Platform.runLater(() -> {
                    try {
                        BorderPane borderPane = new FXMLLoader(HelloApplication.class.getResource("option.fxml")).load();
                        StackPane red = (StackPane)borderPane.lookup("#red");
                        red.setOnMouseClicked(mouseEvent -> {
                            try {
                                //outputStream.write(0);

                                if (answer.equals("0") && b.get()) {
                                    long ended = System.currentTimeMillis();
                                    currpoint = (int)((30000 - (ended-starttime))/100);
                                    point = point + currpoint;
                                    current = true;
                                }
                                else {
                                    current = false;
                                }
                                b.set(false);
                                stage.setScene(new Scene(waitAnswer(), 600, 480));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        StackPane blue = (StackPane)borderPane.lookup("#blue");
                        blue.setOnMouseClicked(mouseEvent -> {
                            try {
                                if (answer.equals("1") && b.get()) {
                                    current = true;
                                    long ended = System.currentTimeMillis();
                                    currpoint = (int)((30000 - (ended-starttime))/100);
                                    point = point + currpoint;
                                }
                                else {
                                    current = false;
                                }
                                b.set(false);
                                stage.setScene(new Scene(waitAnswer(), 600, 480));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        StackPane yellow = (StackPane)borderPane.lookup("#yellow");
                        yellow.setOnMouseClicked(mouseEvent -> {
                            try {
                                //outputStream.write(2);
                                if (answer.equals("2") && b.get()) {
                                    long ended = System.currentTimeMillis();
                                    currpoint = (int)((30000 - (ended-starttime))/100);
                                    point = point + currpoint;
                                    current = true;
                                }
                                else {
                                    current = false;
                                }
                                b.set(false);
                                stage.setScene(new Scene(waitAnswer(), 600, 480));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        StackPane green = (StackPane)borderPane.lookup("#green");
                        green.setOnMouseClicked(mouseEvent -> {
                            try {
                                //outputStream.write(3);
                                if (answer.equals("3") && b.get()) {
                                    long ended = System.currentTimeMillis();
                                    currpoint = (int)((30000 - (ended-starttime))/100);
                                    point = point + currpoint;
                                    current = true;
                                }
                                else {
                                    current = false;
                                }
                                b.set(false);
                                stage.setScene(new Scene(waitAnswer(), 600, 480));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        stage.setScene(new Scene(borderPane, 600, 480));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });
            }
            else if (command.equals("FillIn")) {
                Platform.runLater(() -> {
                    try {
                        BorderPane borderPane = new FXMLLoader(HelloApplication.class.getResource("fillin.fxml")).load();
                        Button send = (Button) borderPane.lookup("#send");
                        TextField textField = (TextField) borderPane.lookup("#textField");
                        send.setOnAction(actionEvent -> {
                            try {
                                //outputStream.writeUTF(textField.getText());
                                if (answer.equalsIgnoreCase(textField.getText()) && b.get()) {
                                    long ended = System.currentTimeMillis();
                                    currpoint = (int)((30000 - (ended-starttime))/100);
                                    point = point + currpoint;
                                    current = true;
                                }
                                else {
                                    current = false;
                                }
                                b.set(false);
                                stage.setScene(new Scene(waitAnswer(), 600, 480));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        stage.setScene(new Scene(borderPane, 600, 480));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });
            }
            else if (command.equals("TrueFalse")) {
                Platform.runLater(() -> {
                    BorderPane borderPane = null;
                    try {
                        borderPane = new FXMLLoader(HelloApplication.class.getResource("truefalse.fxml")).load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    StackPane trueS = (StackPane)borderPane.lookup("#True");
                trueS.setOnMouseClicked(mouseEvent -> {
                    try {
                        //outputStream.write(1);
                        if (answer.equals("1") && b.get()) {
                            long ended = System.currentTimeMillis();
                            currpoint = (int)((30000 - (ended-starttime))/100);
                            point = point + currpoint;
                            current = true;
                        }
                        else {
                            current = false;
                        }
                        b.set(false);
                        stage.setScene(new Scene(waitAnswer(), 600, 480));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                StackPane falseS = (StackPane)borderPane.lookup("#False");
                falseS.setOnMouseClicked(mouseEvent -> {
                    try {
                       // outputStream.write(0);
                        if (answer.equals("0") && b.get()) {
                            long ended = System.currentTimeMillis();
                            currpoint = (int)((30000 - (ended-starttime))/100);
                            point = point + currpoint;
                            current = true;
                        }
                        else {
                            current = false;
                        }
                        b.set(false);
                        stage.setScene(new Scene(waitAnswer(), 600, 480));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                stage.setScene(new Scene(borderPane, 600, 480));
                });
            }
        }
    }
    public BorderPane waitAnswer() {
        try {
            return new FXMLLoader(HelloApplication.class.getResource("waitanswer.fxml")).load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

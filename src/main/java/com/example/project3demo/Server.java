package com.example.project3demo;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private final int port;
    private final ServerSocket serverSocket;
    private ArrayList<Client> clients = new ArrayList<>();
    private final ArrayList<Question> questions;
    private Accepting accepting;
    private FlowPane flowPane;
    private Label count;
    private Stage stage;
    boolean tyu = true;
    int gen = 0;
    Thread thread;
    public Server(int port, ArrayList<Question> questions, FlowPane flowPane, Label count, Stage stage) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(this.port);
        this.questions = questions;
        this.flowPane = flowPane;
        this.count = count;
        this.stage  = stage;
    }
    public void turnON() throws IOException {
        this.accepting = new Accepting(serverSocket, clients, questions, flowPane, count);
        this.accepting.start();
    }

    public void stopAccepting() throws IOException {
        removing();
        accepting.stopAccepting();
        System.out.println("stopped!");
    }
    public void removing() throws IOException {
        String name = "removingBot";
        Socket socket = new Socket("127.0.0.1", port);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(name);
        socket.close();
    }
    public void trying(int i) throws IOException {
        gen = i;
        new Thread(() -> {
            long s = System.currentTimeMillis();
            while (true) {
                if (System.currentTimeMillis()-s>7000){
                    break;
                }
            }
            try {
                trying(i+1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        long start = System.currentTimeMillis();
        AtomicBoolean sw = new AtomicBoolean(true);
         Thread thread = new Thread(() -> {
            while (sw.get()) {
                if (System.currentTimeMillis()-start>5000) {
                    try {
                        trying(i+1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        });
         //thread.start();
        if (i == questions.size()){
            for (Client c: clients) {
                c.dataOutputStream.writeUTF("stop");
                c.dataOutputStream.writeUTF("stop");
                int ip = c.dataInputStream.readInt();
                c.point = ip;
            }
            Collections.sort(clients, new Comparator<Client>() {
                @Override
                public int compare(Client o1, Client o2) {
                    int r = 0;
                    if (o1.point>o2.point) {
                        r = -1;
                    }
                    else if (o1.point<o2.point) {
                        r = 1;
                    }
                    return r;
                }
            });

            BorderPane borderPane = new FXMLLoader(HelloApplication.class.getResource("result.fxml")).load();
            String first = clients.get(0).toString() + " " + clients.get(0).point;
            String second = null;
            try {
                second = clients.get(1).toString()+ " " + clients.get(1).point;
            }
            catch (Exception e){

            }

            String third = null;
            try {
                third = clients.get(2).toString() + " " + clients.get(2).point;
            }
            catch (Exception e) {

            }
            Label f = (Label) borderPane.lookup("#first");
            Label s = (Label) borderPane.lookup("#second");
            Label t = (Label) borderPane.lookup("#third");
            f.setText(first);
            try {
                s.setText(second);
            }catch (Exception e) {

            }
            try {
                t.setText(third);
            }
            catch (Exception e) {

            }
            Platform.runLater(() -> {
                stage.setScene(new Scene(borderPane, 600, 480));
            });
        }
        if (questions.get(i) instanceof Test) {
            AtomicInteger a = new AtomicInteger();
            Platform.runLater(() -> {

                try {
                    BorderPane borderPane = new FXMLLoader(HelloApplication.class.getResource("question.fxml")).load();
                    Label question = (Label)borderPane.lookup("#question");
                    question.setText(questions.get(i).getDescription());
                    ArrayList<String> options = new ArrayList<>();
                    options.add(questions.get(i).getAnswer());
                    options.add(((Test) questions.get(i)).getOptionAt(0));
                    options.add(((Test) questions.get(i)).getOptionAt(1));
                    options.add(((Test) questions.get(i)).getOptionAt(2));
                    Collections.shuffle(options);
                    Label red = (Label)borderPane.lookup("#red");
                    red.setText(options.get(0));
                    Label blue = (Label)borderPane.lookup("#blue");
                    blue.setText(options.get(1));
                    Label green = (Label)borderPane.lookup("#green");
                    green.setText(options.get(2));
                    Label yellow = (Label)borderPane.lookup("#yellow");
                    yellow.setText(options.get(3));
                    for (int j = 0; j < 4; j++) {
                        if (options.get(j).equals(questions.get(i).getAnswer())) {
                            a.set(j);
                        }
                    }
                    Button skip = (Button)borderPane.lookup("#skip");
                    skip.setOnAction(e -> {
                        try {

                            for (Client c : clients) {
                                c.dataOutputStream.writeUTF("skipped");
                                c.dataOutputStream.writeUTF("skipped");
                            }
                            BorderPane borderPane1 = new FXMLLoader(HelloApplication.class.getResource("thrower.fxml")).load();
                            Button button = (Button)borderPane1.lookup("#next");
                            Label label = (Label)borderPane1.lookup("#question") ;
                            label.setText(questions.get(i).getDescription());
                            stage.setScene(new Scene(borderPane1, 700, 700));
                            button.setOnAction(actionEvent -> {
                                try {
                                    trying(i+1);
                                    sw.set(false);

                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            });

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
                    stage.setScene(new Scene(borderPane, 700, 700));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            for (Client c : clients) {
                c.dataOutputStream.writeUTF("Test");
                c.dataOutputStream.writeUTF(String.valueOf(a.get()));
            }
        }
        else if (questions.get(i) instanceof FillIn) {
            Platform.runLater(() -> {
                try {
                    BorderPane borderPane = new FXMLLoader(HelloApplication.class.getResource("FillSc.fxml")).load();
                    Label question = (Label)borderPane.lookup("#question");
                    question.setText(questions.get(i).getDescription());
                    Button skip = (Button)borderPane.lookup("#skip");
                    skip.setOnAction(e -> {
                        try {

                            for (Client c : clients) {
                                c.dataOutputStream.writeUTF("skipped");
                                c.dataOutputStream.writeUTF("skipped");
                            }
                            BorderPane borderPane1 = new FXMLLoader(HelloApplication.class.getResource("thrower.fxml")).load();
                            Button button = (Button)borderPane1.lookup("#next");
                            Label label = (Label)borderPane1.lookup("#question") ;
                            label.setText(questions.get(i).getDescription());
                            stage.setScene(new Scene(borderPane1, 700, 700));
                            button.setOnAction(actionEvent -> {
                                try {
                                    trying(i+1);
                                    sw.set(false);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            });

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
                    stage.setScene(new Scene(borderPane, 700, 700));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            for (Client c : clients) {
                c.dataOutputStream.writeUTF("FillIn");
                c.dataOutputStream.writeUTF(questions.get(i).getAnswer());
            }
        }
        else if (questions.get(i) instanceof TrueFalse) {
            Platform.runLater(() -> {
                try {
                    BorderPane borderPane = new FXMLLoader(HelloApplication.class.getResource("Tscene.fxml")).load();
                    Label question = (Label)borderPane.lookup("#question");
                    question.setText(questions.get(i).getDescription());
                    Button skip = (Button)borderPane.lookup("#skip");
                    skip.setOnAction(e -> {
                        try {

                            for (Client c : clients) {
                                c.dataOutputStream.writeUTF("skipped");
                                c.dataOutputStream.writeUTF("skipped");
                            }
                            BorderPane borderPane1 = new FXMLLoader(HelloApplication.class.getResource("thrower.fxml")).load();
                            Button button = (Button)borderPane1.lookup("#next");
                            Label label = (Label)borderPane1.lookup("#question") ;
                            label.setText(questions.get(i).getDescription());
                            stage.setScene(new Scene(borderPane1, 700, 700));
                            button.setOnAction(actionEvent -> {
                                try {
                                    trying(i+1);
                                    sw.set(false);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            });

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
                    stage.setScene(new Scene(borderPane, 700, 700));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            for (Client c : clients) {
                c.dataOutputStream.writeUTF("TrueFalse");
                if (questions.get(i).getAnswer().equals("True")) {
                    c.dataOutputStream.writeUTF("1");
                }
                else {
                    c.dataOutputStream.writeUTF("0");
                }
            }
        }
    }

    public void close() throws IOException {
        this.serverSocket.close();
    }
}
class Accepting extends Thread implements Runnable{
    private ServerSocket serverSocket;
    private ArrayList<Client> clients;
    private ArrayList<Question> questions;
    private boolean Switch = true;
    private FlowPane flowPane;
    private Label count;
    public Accepting(ServerSocket serverSocket, ArrayList<Client> clients, ArrayList<Question> questions, FlowPane flowPane, Label count) {
        this.serverSocket = serverSocket;
        this.clients = clients;
        this.flowPane = flowPane;
        this.count = count;
    }
    @Override
    public void run() {
        while (Switch) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                Socket finalSocket = socket;
                new Thread(() -> {
                    try {
                        DataInputStream inputStream = new DataInputStream(finalSocket.getInputStream());
                        String name = inputStream.readUTF();
                        Client client = new Client(name, finalSocket, questions);
                        if (!name.equals("removingBot")) {
                            clients.add(client);
                            Platform.runLater(() -> nickToScene(name));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void nickToScene(String name) {
        int temp = Integer.parseInt(count.getText());
        temp++;
        count.setText(String.valueOf(temp));
        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: #0000001a");
        Label label1 = new Label(name);
        label1.setFont(new Font("Arial Bold", 18));
        label1.setStyle("-fx-text-fill: #f2f2f2");
        stackPane.getChildren().add(label1);
        stackPane.setPadding(new Insets(10));
        FlowPane.setMargin(stackPane, new Insets(10));
        flowPane.getChildren().add(stackPane);
    }
    public void stopAccepting(){
        this.Switch = false;
    }
}

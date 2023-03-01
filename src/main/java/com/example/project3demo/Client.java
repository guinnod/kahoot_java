package com.example.project3demo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Thread implements Runnable {
    private final String name;
    private final Socket socket;
    final DataOutputStream dataOutputStream;
    final DataInputStream dataInputStream;
    private ArrayList<Question> questions;
    int point;
    String question;
    String answer;
    boolean b;
    Quiz quiz;
    public Client(String name, Socket socket, ArrayList<Question> questions) throws IOException {
        this.name = name;
        this.socket = socket;
        this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
        this.dataInputStream = new DataInputStream(this.socket.getInputStream());
        this.questions = questions;
    }
    @Override
    public void run() {
        try {
            while (true) {
                String command = dataInputStream.readUTF();
                if (command.equals("exit")){
                    break;
                }
                else if (command.equals("show")) {
                    int i = dataInputStream.readInt();
                    showQuestion(i);
                }
                else if (command.equals("accept")) {
                    
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showQuestion(int i) throws IOException{
        String question = questions.get(i).getDescription();
        String type = "Test";
        if (questions.get(i) instanceof FillIn) {
            type = "FillIn";
        }
        else if (questions.get(i) instanceof TrueFalse) {
            type = "TrueFalse";
        }
        dataOutputStream.writeUTF(type);
        dataOutputStream.writeUTF(question);
    }
    public void acceptAnswers(int i) throws IOException {
        String answer = questions.get(i).getAnswer();
        /*for (Client client: clients) {
            if (client.scanner.hasNext()){
                System.out.println(client + " has next");
                String clientAnswer = client.scanner.next();
                System.out.println(clientAnswer);
                int time = Integer.parseInt(clientAnswer.substring(clientAnswer.indexOf("}") + 1));
                if (clientAnswer.substring(0, clientAnswer.indexOf("{time}")).equals(answer)){
                    client.point += (time/10);
                }
                System.out.println(client.point);
            }
        }
        for (Client client: clients) {
            client.dataOutputStream.writeInt(client.point);
        }*/
    }
    @Override
    public String toString(){
        return this.name;
    }

    public Socket getSocket() {
        return socket;
    }

}

package com.example.project3demo;

import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Quiz {
     ArrayList<Question> questions = new ArrayList<>();
     public Quiz() {

     }
    public void makeQuiz(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String description = scanner.nextLine();
            String answer = scanner.nextLine();
            try {
                String nextStr = scanner.nextLine();
                if (nextStr.equals("")) {
                    FillIn fillIn = new FillIn();
                    fillIn.setDescription(description);
                    fillIn.setAnswer(answer);
                    if (answer.equals("True")||answer.equals("False")) {
                        TrueFalse trueFalse = new TrueFalse();
                        trueFalse.setAnswer(answer);
                        trueFalse.setDescription(description);
                        questions.add(trueFalse);
                    }
                    else {
                        questions.add(fillIn);
                    }
                }
                else {
                    Test test = new Test();

                    test.setAnswer(answer);

                    String n1 = scanner.nextLine();
                    String n2 = scanner.nextLine();
                    if (scanner.hasNextLine()) {
                        scanner.nextLine();
                    }
                    String[] options = {nextStr, n1, n2};
                    test.setOptions(options);
                    if (description.endsWith(".mp3")){
                        test.setDescription("Audio");
                    }
                    else {
                        test.setDescription(description);
                    }
                    questions.add(test);
                }
            }
            catch (Exception e) {
                FillIn fillIn = new FillIn();
                fillIn.setDescription(description);
                fillIn.setAnswer(answer);
                if (answer.equals("True")||answer.equals("False")) {
                    TrueFalse trueFalse = new TrueFalse();
                    trueFalse.setAnswer(answer);
                    trueFalse.setDescription(description);
                    questions.add(trueFalse);
                }
                else {
                    questions.add(fillIn);
                }
            }
        }
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }
}

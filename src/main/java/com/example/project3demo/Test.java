package com.example.project3demo;

public class Test extends Question {
    private String[] options;

    public Test() { }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public String getOptionAt(int index) {
        return options[index];
    }

    @Override
    public String toString() {
        return "Test";
    }
}

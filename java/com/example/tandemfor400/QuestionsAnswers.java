package com.example.tandemfor400;

public class QuestionsAnswers {
    String question, incorrect, correct;

    public QuestionsAnswers(String question, String incorrect, String correct) {
        this.question = question;
        this.incorrect = incorrect;
        this.correct = correct;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getIncorrect() {
        return incorrect;
    }

    public void setIncorrect(String incorrect) {
        this.incorrect = incorrect;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }
}

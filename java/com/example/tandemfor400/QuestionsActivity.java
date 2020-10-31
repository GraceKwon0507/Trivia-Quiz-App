package com.example.tandemfor400;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuestionsActivity extends AppCompatActivity {
    TextView tv;
    TextView questiontv;
    Button submitbutton, quitbutton;
    RadioGroup radio_g;
    RadioButton rb1, rb2, rb3, rb4;

    int count = 0;
    public static int marks = 0, correct = 0, wrong = 0;

    String[] question = new String[21]; // get all the questions
    final String[] correctA = new String[21]; // get all the corrects
    String[][] incorrectA = new String[21][3]; // get all the incorrects
    String[] displayQuestions = new String[10]; // choose 10 random questions
    String chooseQuestion = null; // choose random quesiton
    String[] answerArray = new String[10]; // array of incorrect & correct answers
    String[][] displayAnswers = new String[10][4];
    List answerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        final TextView score = (TextView) findViewById(R.id.textView4);
        TextView textView = (TextView) findViewById(R.id.name);

        Intent intent = getIntent();
        String name = intent.getStringExtra("Name");

        if (name.trim().equals(""))
            textView.setText("Hello User");
        else
            textView.setText("Hello " + name);

        try {
            // get JSONObject from JSON file
            JSONArray array = new JSONArray(loadJSONFromAsset());


            // for loop to get questions and answers
            for (int i = 0; i < array.length(); i++){
                JSONObject qna = array.getJSONObject(i);

                question[i] = qna.getString("question");
                correctA[i]= qna.getString("correct");

                JSONArray incorrectAarray = (JSONArray) qna.get("incorrect");

                for (int j = 0; j <= qna.length(); j++){
                    incorrectA[i][j] = (String) incorrectAarray.get(j);

                    answerList = new ArrayList(Arrays.asList(incorrectA[i]));
                    answerList.addAll(Arrays.asList(correctA[i]));
                    //System.out.println(answerList);
                    Object[] answerObject = answerList.toArray();
                    answerArray[i] = Arrays.toString(answerObject);
                    displayAnswers[i][j] = (String) answerList.get(j);

                }//System.out.println(displayAnswers[i]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // choose 10 questions for each round
        for (int a = 0; a < 10 ; a++){
            chooseQuestion = randomQuestions(question);

            //if(chooseQuestion)
            displayQuestions[a] = chooseQuestion;
            System.out.println(displayQuestions[a]);
        }

        // get each element
        submitbutton = (Button) findViewById(R.id.submitButton);
        quitbutton = (Button) findViewById(R.id.quitButton);

        radio_g = (RadioGroup) findViewById(R.id.radioGroup);
        rb1 = (RadioButton) findViewById(R.id.radioButton);
        rb2 = (RadioButton) findViewById(R.id.radioButton2);
        rb3 = (RadioButton) findViewById(R.id.radioButton3);
        rb4 = (RadioButton) findViewById(R.id.radioButton4);

        questiontv = (TextView) findViewById(R.id.questions);

        // apply changes to textview so that the questions and answers change accordingly
        questiontv.setText(displayQuestions[0]);

//        rb1.setText((String) answerList.get(0));
//        rb2.setText(answerArray[count][1]);
//        rb3.setText(answerArray[count][2]);
//        rb4.setText(answerArray[count][3]);

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radio_g.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select one answer", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton userAnswer = (RadioButton) findViewById(radio_g.getCheckedRadioButtonId());
                String userAnswerString = userAnswer.getText().toString();
//                Toast.makeText(getApplicationContext(), ansText, Toast.LENGTH_SHORT).show();


                if(userAnswerString.equals(correctA[count])) {
                    correct++;
                    Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                }
                else {
                    wrong++;
                    Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
                }

                count++;

                if (score != null)
                    score.setText("" + correct);

                if(count<question.length)
                {
                    questiontv.setText(displayQuestions[count]);
//                    rb1.setText(answerArray[count][0]);
//                    rb2.setText(answerArray[count][1]);
//                    rb3.setText(answerArray[count][2]);
//                    rb4.setText(answerArray[count][3]);
                }
                else
                {
                    marks = correct;
                    Intent in = new Intent(getApplicationContext(),ResultActivity.class);
                    startActivity(in);
                }

                radio_g.clearCheck();
            }
        });

        quitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                startActivity(intent);
            }
        });
    }

    // method to load JSON file from assets folder
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("Apprentice_TandemFor400_Data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    // randomize questions
    public static String randomQuestions(String [] array) {
        Random generator = new Random();
        int randomIndex = generator.nextInt(array.length);
        return array[randomIndex];
    }

    // shuffle answers
//    public void shuffleAnswers() {
//        Collections.shuffle(this.answers);
//    }
}

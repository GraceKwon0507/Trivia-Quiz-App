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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QuestionsActivity extends AppCompatActivity {
    TextView tv;
    TextView questiontv;
    Button submitbutton, quitbutton;
    RadioGroup radio_g;
    RadioButton rb1, rb2, rb3, rb4;
    int count = 0;
    public static int marks = 0, correct = 0, wrong = 0;

    String userAnswerString;

    HashMap<String, List<String>> questionToAnswers = new HashMap<>(); // hashmap for question to incorrects and corrects
    HashMap<String, String> questionToCorrectAnswer = new HashMap<>(); // hashmap for question to corrects
    LinkedHashMap<String, List<String>> shuffledQuestionToAnswers = new LinkedHashMap<String, List<String>>(); // hashmap for shuffled question to incorrects and corrects

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
                JSONObject qna = array.getJSONObject(i); // get JSONobject

                List<String> answersList = new ArrayList<>();

                String questionString = qna.getString("question"); // get questions

                answersList.add(qna.getString("correct")); // get corrects

                List<String> incorrectList;

                // get incorrects
                String[] incorrectsStringArray = qna.getString("incorrect").replace("[","").replace("]","").split("\",\"");

                incorrectsStringArray[0] =  incorrectsStringArray[0].replace("\"","");
                incorrectsStringArray[incorrectsStringArray.length - 1] = incorrectsStringArray[incorrectsStringArray.length - 1].replace("\"", "");

                incorrectList = Arrays.asList(incorrectsStringArray);

                answersList.addAll(incorrectList);

                questionToAnswers.put(questionString, answersList);
                questionToCorrectAnswer.put(questionString, qna.getString("correct"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        submitbutton = (Button) findViewById(R.id.submitButton);
        quitbutton = (Button) findViewById(R.id.quitButton);

        radio_g = (RadioGroup) findViewById(R.id.radioGroup);
        rb1 = (RadioButton) findViewById(R.id.radioButton);
        rb2 = (RadioButton) findViewById(R.id.radioButton2);
        rb3 = (RadioButton) findViewById(R.id.radioButton3);
        rb4 = (RadioButton) findViewById(R.id.radioButton4);

        questiontv = (TextView) findViewById(R.id.questions);

        shuffleQuestionsAndAnswers();

        setNewAnswers();

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radio_g.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select one answer", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton userAnswer = (RadioButton) findViewById(radio_g.getCheckedRadioButtonId());
                userAnswerString = userAnswer.getText().toString();
//                Toast.makeText(getApplicationContext(), ansText, Toast.LENGTH_SHORT).show();

                Set<String> questionToAnswerKeySet = shuffledQuestionToAnswers.keySet();

                Object[] questionToCorrectAnswerKeyArray = questionToAnswerKeySet.toArray();

                for(int i = 0; i < questionToCorrectAnswerKeyArray.length; i++){
                    if(i == count){
                        if (userAnswerString.equals(shuffledQuestionToAnswers.get(questionToCorrectAnswerKeyArray[i]).get(0))) {
                            Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                            correct++;
                        } else {
                            Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
                            wrong++;
                        }
                    }
                    else if (i > count){
                        break;
                    }
                }
                count++;

                setNewAnswers();

                if (score != null)
                    score.setText("" + correct);

                radio_g.clearCheck();

                if(count == 10){
                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    startActivity(intent);
                }
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

    public void shuffleQuestionsAndAnswers(){
        List<Map.Entry<String, List<String>>> list = new ArrayList<>(questionToAnswers.entrySet());

        // shuffle questions
        Collections.shuffle(list);

        for (Map.Entry<String, List<String>> entry : list) {
            shuffledQuestionToAnswers.put(entry.getKey(), entry.getValue());
        }
    }

    // method for radio buttons
    // so that the answers get replaced according to the question
    public void setNewAnswers(){
        Set<String> keySet = shuffledQuestionToAnswers.keySet();

        Object[] keyArray = keySet.toArray();

        for(int i = 0; i < keyArray.length; i++){

            // check if i equals to count
            if(i == count){
                // get questions
                // display question # : i+1
                questiontv.setText(i+1 + ". " + (CharSequence) keyArray[i]);

                // get answers
                rb1.setText(shuffledQuestionToAnswers.get(keyArray[i]).get(0));
                rb2.setText(shuffledQuestionToAnswers.get(keyArray[i]).get(1));
                // check if the third incorrect is null
                if(shuffledQuestionToAnswers.get(keyArray[i]).size() == 3 || shuffledQuestionToAnswers.get(keyArray[i]).size() == 4) {
                    rb3.setText(shuffledQuestionToAnswers.get(keyArray[i]).get(2));
                }else if(shuffledQuestionToAnswers.get(keyArray[i]).size() != 3 || shuffledQuestionToAnswers.get(keyArray[i]).size() != 4){
                    rb3.setText("");
                }
                if(shuffledQuestionToAnswers.get(keyArray[i]).size() == 4){
                    rb4.setText(shuffledQuestionToAnswers.get(keyArray[i]).get(3));
                }else if(shuffledQuestionToAnswers.get(keyArray[i]).size() != 4){
                    rb4.setText("");
                }
            } else if(i > count){
                break;
            }
        }
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

}

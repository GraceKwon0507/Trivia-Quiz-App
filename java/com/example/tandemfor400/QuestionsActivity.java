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

public class QuestionsActivity extends AppCompatActivity {
    TextView tv;
    TextView questiontv;
    Button submitbutton, quitbutton;
    RadioGroup radio_g;
    RadioButton rb1, rb2, rb3, rb4;
    int count = 0;
    public static int marks = 0, correct = 0, wrong = 0;

    String[] question = new String[21];
    final String[] correctA = new String[21];
    String[][] incorrectA = new String[21][3];

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
                System.out.println(correctA[i]);

                JSONArray incorrectAarray = (JSONArray) qna.get("incorrect");

                for (int j = 0; j < qna.length(); j++){
                    incorrectA[i][j] = (String) incorrectAarray.get(j);
//
//                    int incLen = incorrectA[i][j].length();
//                    int cLen = correctA[i].length();
//
//                    String[] mcq = new String [incLen + cLen];

//                    System.out.println(incorrectA[i][j]);
//                    System.out.println(incorrectA);
//                    System.arraycopy(incorrectA, 0, mcq, 0, incLen);
//                    System.arraycopy(correctA, 0, mcq, 0, cLen);
//
//                    System.out.println(mcq[i]);
                }
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

        questiontv.setText(question[0]);

        rb1.setText(incorrectA[0][0]);
        rb2.setText(incorrectA[0][1]);
        rb3.setText(incorrectA[0][2]);
        rb4.setText(correctA[0]);

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
                    questiontv.setText(question[count]);
                    rb1.setText(incorrectA[count][0]);
                    rb2.setText(incorrectA[count][1]);
                    rb3.setText(incorrectA[count][2]);
                    rb4.setText(correctA[count]);
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
}

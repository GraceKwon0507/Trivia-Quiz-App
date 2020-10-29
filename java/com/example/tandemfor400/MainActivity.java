package com.example.tandemfor400;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startbutton=(Button)findViewById(R.id.startButton);
        final EditText nameText = (EditText)findViewById(R.id.name);

        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = nameText.getText().toString();
                Intent intent = new Intent(getApplicationContext(), QuestionsActivity.class);
                intent.putExtra("Name", nameString);
                startActivity(intent);
            }
        });
    }
}
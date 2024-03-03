package com.example.navigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Feedback extends AppCompatActivity {


    EditText etSub, etMes;

    TextView profile_name;
    String namee;
    Button btnsubmitefeedback;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        //profile_name = (TextView) findViewById(R.id.profile_name);

        etSub = findViewById(R.id.etSubject);
        etMes = findViewById(R.id.etMessage);
        btnsubmitefeedback = findViewById(R.id.btnSubmiteFeedback);
        dbHelper = new DBHelper(this);
        btnsubmitefeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subject,message;
                subject = etSub.getText().toString();
                message = etMes.getText().toString();
                if (subject.equals("") || message.equals("")) {
                    Toast.makeText(Feedback.this, "please fill all the fields", Toast.LENGTH_LONG).show();
                }
                boolean FeedbackSuccess;
                if (dbHelper.insertData(subject,message)) FeedbackSuccess = true;
                else FeedbackSuccess = false;
                if (FeedbackSuccess)
                    Toast.makeText(Feedback.this, "User Feedback Successfully", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(Feedback.this, "User Feedback failed", Toast.LENGTH_LONG).show();
            }

        });



    }



}
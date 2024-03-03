package com.example.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityLogin extends AppCompatActivity {

    DBHelper dbHelper;

    Button btnLogin,btngotoregister;


    EditText etUsername, etPwd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DBHelper(this);
        etUsername = findViewById(R.id.etUsername);
        etPwd = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btngotoregister =findViewById(R.id.btnGoToRegister);



        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                boolean isLoggedId = dbHelper.checkUser(etUsername.getText().toString(),etPwd.getText().toString());
                boolean isAdminid = dbHelper.checkAdmin(etPwd.getText().toString());

                if (isAdminid){
                    Intent intent = new Intent(ActivityLogin.this,admin.class);
                    startActivity(intent);
                }
                else if(isLoggedId){
                    Intent intent = new Intent(ActivityLogin.this,MainActivity.class);

                    startActivity(intent);

                }
                else
                    Toast.makeText(ActivityLogin.this,"Login Failed",Toast.LENGTH_LONG).show();
            }
        });


        btngotoregister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                    Intent intent = new Intent(ActivityLogin.this,SingupActivity.class);
                    startActivity(intent);
            }
        });


    }


}

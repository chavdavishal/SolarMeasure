package com.example.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SingupActivity extends AppCompatActivity{

    EditText etUser, etemail , etmobile , etPwd, etRepwd;

    Button btnRegister,btnGoToLogin;
    DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
        etUser = findViewById(R.id.etUsername);
        etemail = findViewById(R.id.etEmail);
        etmobile = findViewById(R.id.etMobileNo);
        etPwd = findViewById(R.id.etPassword);
        etRepwd = findViewById(R.id.etRePassword);
        btnRegister = findViewById(R.id.btnRegister);
        dbHelper = new DBHelper(this);
        btnGoToLogin = findViewById(R.id.btnLogin);
        btnGoToLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(SingupActivity.this, ActivityLogin.class);
                startActivities(new Intent[]{intent});
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user,email,mobile, pwd, rePwd;
                user = etUser.getText().toString();
                email = etemail.getText().toString();
                mobile = etmobile.getText().toString();
                pwd = etPwd.getText().toString();
                rePwd = etRepwd.getText().toString();
                if (user.equals("") || email.equals("")|| mobile.equals("") || pwd.equals("") || rePwd.equals(""))
                {
                    Toast.makeText(SingupActivity.this, "please fill all the fields", Toast.LENGTH_LONG).show();
                }
                if (!user.matches("[a-z]+"))
                {
                    etUser.requestFocus();
                    etUser.setError("ENTER ONLY ALPHABETICAL CHARACTER");
                }
                if (!email.matches("[a-z0-9._-]+@[a-z]+\\.+[a-z]+"))
                {
                    etemail.requestFocus();
                    etemail.setError("not valid enter like:abc12@gmail.com");
                }
                if (!mobile.matches("[0-9]{10}+"))
                {
                    etmobile.requestFocus();
                    etmobile.setError("ENTER 10 digits Number");
                }
                if (!pwd.matches("(?=.*[@#$%^&+=])" + "(?=\\S+$)"+".{6,10}"+"$"))
                {
                    etPwd.requestFocus();
                    etPwd.setError("ENTER 1 special char and password is less then 6 digits and more then 10");
                }
                else
                {
                    if (pwd.equals(rePwd))
                    {

                            Toast.makeText(SingupActivity.this, "User already exists", Toast.LENGTH_LONG).show();

                        //proceed with registration
                        boolean registeredSuccess;
                        if (dbHelper.insertData(user, email, mobile, pwd))
                        {
                            Intent intent = new Intent(SingupActivity.this, ActivityLogin.class);
                            startActivity(intent);
                            registeredSuccess = true;
                        }
                        else registeredSuccess = false;
                        if (registeredSuccess)
                        {
                            Toast.makeText(SingupActivity.this, "User Registered Successfully", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(SingupActivity.this, "User Registered failed", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        etRepwd.requestFocus();
                        etRepwd.setError("Enter same password");
                        Toast.makeText(SingupActivity.this, "password do not match", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }
}
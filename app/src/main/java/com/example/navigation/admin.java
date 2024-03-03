package com.example.navigation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class admin extends AppCompatActivity {

    private EditText nametext,contacttext,emailtext,addresstext;
    private ImageView imageView;
    private static final int PICK_IMAGE_REQUEAST = 100;
    private Uri imageFilePath;
    private Bitmap imageToStore;
    DBHelper objectdbhelper;
    private Button StoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);




        nametext = findViewById(R.id.nametext);
        contacttext = findViewById(R.id.contacttext);
        emailtext = findViewById(R.id.emailtext);
        addresstext = findViewById(R.id.addresstext);
        imageView = findViewById(R.id.imageView);
        StoreButton = findViewById(R.id.saveBtn);

        StoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeCompany();
            }
        });

        objectdbhelper=new DBHelper(this);
    }

    public void storeCompany() {

            if (!nametext.getText().toString().isEmpty() && imageView.getDrawable()!=null && imageToStore!=null)
            {
                objectdbhelper.storeCompany(new ModelClass(nametext.getText().toString(),contacttext.getText().toString(),emailtext.getText().toString(),addresstext.getText().toString(),imageToStore));
            }
            else
            {
                Toast.makeText(this,"please select name and image",Toast.LENGTH_SHORT).show();
            }



    }

    public void chooseImage(View objectView){

            Intent objectIntent=new Intent();
            objectIntent.setType("image/*");

            objectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(objectIntent,PICK_IMAGE_REQUEAST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEAST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imageFilePath = data.getData();
            try {
                imageToStore= MediaStore.Images.Media.getBitmap(getContentResolver(),imageFilePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imageView.setImageBitmap(imageToStore);
        }

    }

    public void moveToShowActivity(View view)
    {

        startActivity(new Intent(this, MainActivity.class));

    }
    public void addProductActivity(View view)
    {
        startActivity(new Intent(this, MainActivity2.class));
    }

}
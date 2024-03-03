package com.example.navigation;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;

public class MainActivity2 extends AppCompatActivity {
    private DBHelper objectDatabaseHelper;
    private RecyclerView objectRecyclerView;
    private RVAdapter objectRvAdapter;

    private RVPAdapter objectRvpAdapter;
    private EditText cid,pname,pprice,pwatts;
    private ImageView imageView;
    private static final int PICK_IMAGE_REQUEAST = 100;
    private Uri imageFilePath;
    private Bitmap imageToStore;
    DBHelper objectdbhelper;
    private Button StoreButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


            cid=findViewById(R.id.Cid);
            pname=findViewById(R.id.Pname);
            pprice=findViewById(R.id.Pprice);
            pwatts=findViewById(R.id.Pwatts);

            imageView=findViewById(R.id.imageView);
            StoreButton=findViewById(R.id.storeBtn);

            StoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    storeProduct();
                }
            });

            objectdbhelper=new DBHelper(this);




    }
    public void chooseImage(View objectView){
            Intent objectIntent=new Intent();
            objectIntent.setType("image/*");

            objectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(objectIntent,PICK_IMAGE_REQUEAST);


    }

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

    private void storeProduct() {
            if (!cid.getText().toString().isEmpty() && imageView.getDrawable()!=null && imageToStore!=null)
            {
                objectdbhelper.storeProduct(new ProductModel(cid.getText().toString(),pname.getText().toString(),pprice.getText().toString(),pwatts.getText().toString(),imageToStore));
            }
            else
            {
                Toast.makeText(this,"please select name and image",Toast.LENGTH_SHORT).show();
            }


    }

    public void moveToShowActivity(View view)
    {

        startActivity(new Intent(this, MainActivity.class));

    }


}
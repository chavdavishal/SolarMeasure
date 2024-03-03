package com.example.navigation;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProductView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
        byte[] image;
        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        String watts = getIntent().getStringExtra("watts");
        String desc = getIntent().getStringExtra("desc");
        int images = getIntent().getIntExtra("image",0);

        TextView nameText = findViewById(R.id.Productname);
        TextView PriceText = findViewById(R.id.Productprice);
        TextView wattsText = findViewById(R.id.Productwatts);
        TextView descr = findViewById(R.id.desc);

        ImageView imageview = findViewById(R.id.Productimage);
        //Bitmap objectBitmap = BitmapFactory.decodeByteArray(image,0,image.length);


        nameText.setText(name);
        PriceText.setText(price);
        wattsText.setText(watts);
        descr.setText(desc);
        imageview.setImageResource(images);


    }
}
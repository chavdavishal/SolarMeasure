package com.example.navigation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNome = "solar.db";

    Context context;

    private static String companytable="create table company (cid integer primary key AUTOINCREMENT,imageName TEXT ,Contact text,Email TEXT,Addres TEXT,image BLOB)";
    private static String producttable="create table product (pid integer primary key AUTOINCREMENT,cid integer,productname TEXT ,price TEXT,watts TEXT,image BLOB,FOREIGN KEY (cid) REFERENCES company (cid))";


    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imageInBytes;

    public DBHelper(@Nullable Context context)
    {
        super(context, DBNome, null,1);
        this.context=context;

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("create table users(username TEXT ,Email TEXT,Mobile TEXT,password TEXT,gender TEXT)");
        sqLiteDatabase.execSQL("create table feedback(subject TEXT,message TEXT)");
        sqLiteDatabase.execSQL("INSERT INTO users (username, email, mobile, password, gender)\n" +
                "VALUES ('vishal', 'vishalchavda2003@gmail.com', '8238001500', '@vis123', 'Admin');\n");
        sqLiteDatabase.execSQL("INSERT INTO users (username, email, mobile, password, gender)\n" +
                "VALUES ('vivek', 'vivek123@gmail.com', '1234567890', '@pass12', 'Admin');\n");
        sqLiteDatabase.execSQL(companytable);
        sqLiteDatabase.execSQL(producttable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int i,int i1)
    {
        sqLiteDatabase.execSQL("drop table if exists users");
        sqLiteDatabase.execSQL("drop table if exists feedback");

    }

    public boolean insertData(String username , String Email , String Mobile ,String password ){
        SQLiteDatabase mtDB = this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put("username",username);
        contentValues.put("email",Email);
        contentValues.put("mobile",Mobile);
        contentValues.put("password",password);
        long result = mtDB.insert("users",null,contentValues);

        if (result == -1) return  false;
        else return true;

    }



    public boolean insertData(String subject,String message){
        SQLiteDatabase mtdb = this.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("subject",subject);
        contentValues1.put("message",message);
        long result2 = mtdb.insert("feedback",null,contentValues1);
        if (result2 == -1)return false;
        else return true;
    }

    public boolean checkUsername(String username){
        SQLiteDatabase myDB =  this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("select * from users where username = ?",new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else return false;
    }

    public boolean checkAdmin(String pass){
        SQLiteDatabase mydb1 = this.getWritableDatabase();
        Cursor cursor =mydb1.rawQuery("select * from users where password = ? AND gender = 'Admin'",new String[]{pass});
        if (cursor.getCount()> 0)
            return true;
        else return false;
    }

    public boolean checkUser(String username, String pwd){
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("select * from users where username = ? and password=?",new String[]{username,pwd});
        if (cursor.getCount() > 0)
            return true;
        else return false;
    }

    public void storeCompany(ModelClass objectModelClass){
        try{
            SQLiteDatabase objectSQLiteDatabase=this.getWritableDatabase();
            Bitmap imagetToStoreBitmap=objectModelClass.getImage();
            objectByteArrayOutputStream=new ByteArrayOutputStream();
            imagetToStoreBitmap.compress(Bitmap.CompressFormat.JPEG,100,objectByteArrayOutputStream);

            imageInBytes=objectByteArrayOutputStream.toByteArray();
            ContentValues objectContentValues = new ContentValues();
            objectContentValues.put("imageName",objectModelClass.getImageName());
            objectContentValues.put("Contact",objectModelClass.getContact());
            objectContentValues.put("Email",objectModelClass.getEmail());
            objectContentValues.put("Addres",objectModelClass.getAddress());
            objectContentValues.put("image",imageInBytes);
            long checkIfQueryRuns=objectSQLiteDatabase.insert("company",null,objectContentValues);
            if(checkIfQueryRuns!=-1){
                Toast.makeText(context,"data inaserted ", Toast.LENGTH_SHORT).show();
                objectSQLiteDatabase.close();
            }
            else { Toast.makeText(context,"failed to add data ", Toast.LENGTH_SHORT).show();}

        }
        catch (Exception e){

        }
    }

    public ArrayList<ModelClass> getAllImagesData()
    {
            SQLiteDatabase objectSqLiteDatabase = this.getReadableDatabase();
            ArrayList<ModelClass> objectModelClassList = new ArrayList<>();

            Cursor objectCursor=objectSqLiteDatabase.rawQuery("select * from company",null);
            if (objectCursor.getCount()!=0)
            {
                while(objectCursor.moveToNext()){
                    String nameOfImage = objectCursor.getString(1);
                    String Contact = objectCursor.getString(2);
                    String Email = objectCursor.getString(3);
                    String Address = objectCursor.getString(4);
                    byte [] imageBytes = objectCursor.getBlob(5);

                    Bitmap objectBitmap = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);

                    objectModelClassList.add(new ModelClass(nameOfImage,Contact,Email,Address,objectBitmap));
                }
                return objectModelClassList;
            }
            else { Toast.makeText(context, "no value exists in database", Toast.LENGTH_SHORT).show();
                return null;}

    }

    public ArrayList<ProductModel> getAllProductData()
    {
        try
        {
            SQLiteDatabase objectSqLiteDatabase = this.getReadableDatabase();
            ArrayList<ProductModel> objectproductClassList = new ArrayList<>();

            Cursor objectCursor=objectSqLiteDatabase.rawQuery("select * from product",null);
            if (objectCursor.getCount()!=0)
            {
                while(objectCursor.moveToNext()){
                    String cid = objectCursor.getString(1);
                    String pname = objectCursor.getString(2);
                    String pprice = objectCursor.getString(3);
                    String pwatts = objectCursor.getString(4);
                    byte [] imageBytes = objectCursor.getBlob(5);

                    Bitmap objectBitmap = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);

                    objectproductClassList.add(new ProductModel(cid,pname,pprice,pwatts,objectBitmap));
                }
                return objectproductClassList;
            }
            else { Toast.makeText(context, "no value exists in database", Toast.LENGTH_SHORT).show();
                return null;}
        }
        catch (Exception e)
        {Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public void storeProduct(ProductModel objectModelClass){

            SQLiteDatabase objectSQLiteDatabase=this.getWritableDatabase();
            Bitmap imagetToStoreBitmap=objectModelClass.getImage();
            objectByteArrayOutputStream=new ByteArrayOutputStream();
            imagetToStoreBitmap.compress(Bitmap.CompressFormat.JPEG,100,objectByteArrayOutputStream);

            imageInBytes=objectByteArrayOutputStream.toByteArray();
            ContentValues objectContentValues = new ContentValues();
            objectContentValues.put("cid",objectModelClass.getCid());
            objectContentValues.put("productname",objectModelClass.getPname());
            objectContentValues.put("price",objectModelClass.getPprice());
            objectContentValues.put("watts",objectModelClass.getPwatts());
            objectContentValues.put("image",imageInBytes);
            long checkIfQueryRuns=objectSQLiteDatabase.insert("product",null,objectContentValues);
            if(checkIfQueryRuns!=-1){
                Toast.makeText(context,"data inaserted ", Toast.LENGTH_SHORT).show();
                objectSQLiteDatabase.close();
            }
            else { Toast.makeText(context,"failed to add data ", Toast.LENGTH_SHORT).show();}



    }





}

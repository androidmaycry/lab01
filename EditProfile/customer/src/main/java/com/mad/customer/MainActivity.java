package com.mad.customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String MyPREF = "User_Data";
    private static final String CheckPREF = "First Run";
    private static final String Name = "keyName";
    private static final String Surname = "keySurname";
    private static final String Address = "keyAddress";
    private static final String Description = "keyDescription";
    private static final String Email = "keyEmail";
    private static final String Phone = "keyPhone";
    private static final String Photo = "keyPhoto";
    private static final String FirstRun = "keyRun";

    private SharedPreferences first_check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        File f = new File("/data/data/com.mad.editprofile/shared_prefs/User_Data.xml");
        if (!f.exists()){
            Intent edit_profile = new Intent(getApplicationContext(), EditProfile.class);
            startActivityForResult(edit_profile, 0);
            first_check = getSharedPreferences(CheckPREF, MODE_PRIVATE);
            if(first_check.getBoolean("flagRun", false )){
                SharedPreferences.Editor editor = first_check.edit();
                editor.putBoolean("flagRun", false);
                editor.apply();
                finishActivity(0);
            }
        }

        SharedPreferences user_data = getSharedPreferences(MyPREF, MODE_PRIVATE);

        ((TextView)findViewById(R.id.name)).setText(user_data.getString(Name, ""));
        ((TextView)findViewById(R.id.surname)).setText(user_data.getString(Surname, ""));
        ((TextView)findViewById(R.id.address)).setText(user_data.getString(Address, ""));
        ((TextView)findViewById(R.id.description)).setText(user_data.getString(Description, ""));
        ((TextView)findViewById(R.id.mail)).setText(user_data.getString(Email, ""));
        ((TextView)findViewById(R.id.phone)).setText(user_data.getString(Phone, ""));
        try {
            setPhoto(user_data.getString(Photo, ""));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setEasterEgg();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.item1:
                Intent edit_profile = new Intent(getApplicationContext(), EditProfile.class);
                startActivityForResult(edit_profile, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 30 && data != null && data.getBooleanExtra(FirstRun,false)){

            SharedPreferences.Editor editor = first_check.edit();
            editor.putBoolean("flagRun", false);
            editor.apply();
            finish();
        }

        if(data != null && resultCode == 1){
            ((TextView)findViewById(R.id.name)).setText(data.getStringExtra(Name));
            ((TextView)findViewById(R.id.surname)).setText(data.getStringExtra(Surname));
            ((TextView)findViewById(R.id.address)).setText(data.getStringExtra(Address));
            ((TextView)findViewById(R.id.description)).setText(data.getStringExtra(Description));
            ((TextView)findViewById(R.id.mail)).setText(data.getStringExtra(Email));
            ((TextView)findViewById(R.id.phone)).setText(data.getStringExtra(Phone));
            try {
                setPhoto(data.getStringExtra(Photo));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setPhoto(String photoPath) throws IOException {
        File imgFile = new File(photoPath);

        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        myBitmap = adjustPhoto(myBitmap, photoPath);

        ((ImageView)findViewById(R.id.profile_image)).setImageBitmap(myBitmap);
    }

    private Bitmap adjustPhoto(Bitmap bitmap, String photoPath) throws IOException {
        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }

        return rotatedBitmap;
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void setEasterEgg(){
        findViewById(R.id.textView3).setOnClickListener(e -> {
            Intent easter_egg = new Intent(getApplicationContext(), EasterEggProfile.class);
            startActivity(easter_egg);
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

}
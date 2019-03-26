package com.mad.editprofile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String MyPREF = "User_Data";

    private static final String Name = "keyName";
    private static final String Surname = "keySurname";
    private static final String Address = "keyAddress";
    private static final String Description = "keyDescription";
    private static final String Email = "keyEmail";
    private static final String Phone = "keyPhone";
    private static final String Photo = "keyPhoto";

    private File imgFile;

    private SharedPreferences user_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File f = new File("/data/data/com.mad.editprofile/shared_prefs/User_Data.xml");
        if (!f.exists()){
            Intent edit_profile = new Intent(getApplicationContext(), EditProfile.class);
            startActivityForResult(edit_profile, 0);
        }

        user_data = getSharedPreferences(MyPREF, MODE_PRIVATE);

        String nm = user_data.getString(Name, "");
        String surnm = user_data.getString(Surname, "");
        String addr = user_data.getString(Address, "");
        String desc = user_data.getString(Description, "");
        String email = user_data.getString(Email, "");
        String phone = user_data.getString(Phone, "");
        String photoPath = user_data.getString(Photo, "");

        ((TextView)findViewById(R.id.name)).setText(nm);
        ((TextView)findViewById(R.id.surname)).setText(surnm);
        ((TextView)findViewById(R.id.address)).setText(addr);
        ((TextView)findViewById(R.id.description)).setText(desc);
        ((TextView)findViewById(R.id.mail)).setText(email);
        ((TextView)findViewById(R.id.phone)).setText(phone);

        imgFile = new File(photoPath);
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        try {
            myBitmap = adjustPhoto(myBitmap, photoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((ImageView)findViewById(R.id.profile_image)).setImageBitmap(myBitmap);
        imgFile = null;
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
                //Toast.makeText(getApplicationContext(),"Item 1 Selected", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null && resultCode == 1){
            String nm = data.getStringExtra(Name);
            String surnm = data.getStringExtra(Surname);
            String addr = data.getStringExtra(Address);
            String desc = data.getStringExtra(Description);
            String email = data.getStringExtra(Email);
            String phone = data.getStringExtra(Phone);
            String photoPath = data.getStringExtra(Photo);

            ((TextView)findViewById(R.id.name)).setText(nm);
            ((TextView)findViewById(R.id.surname)).setText(surnm);
            ((TextView)findViewById(R.id.address)).setText(addr);
            ((TextView)findViewById(R.id.description)).setText(desc);
            ((TextView)findViewById(R.id.mail)).setText(email);
            ((TextView)findViewById(R.id.phone)).setText(phone);

            imgFile = new File(photoPath);
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            try {
                myBitmap = adjustPhoto(myBitmap, photoPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((ImageView)findViewById(R.id.profile_image)).setImageBitmap(myBitmap);
            imgFile = null;
        }
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
}

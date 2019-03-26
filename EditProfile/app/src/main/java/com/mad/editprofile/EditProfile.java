package com.mad.editprofile;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.icu.text.SimpleDateFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class EditProfile extends AppCompatActivity {
    private static final int OK_CHECK = 0;
    private static final int NAME_ERROR = -1;
    private static final int SURNAME_ERROR = -2;
    private static final int ADDR_ERROR = -3;
    private static final int PSW_ERROR = -4;
    private static final int MAIL_ERROR = -5;
    private static final int PHONE_ERROR = -6;

    private static final String MyPREF = "User_Data";
    private static final String Name = "keyName";
    private static final String Surname = "keySurname";
    private static final String Address = "keyAddress";
    private static final String Description = "keyDescription";
    private static final String Password = "keyPassword";
    private static final String Email = "keyEmail";
    private static final String Phone = "keyPhone";
    private static final String Photo = "keyPhoto";

    private String name;
    private String surname;
    private String addr;
    private String desc;
    private String psw;
    private String mail;
    private String phone;
    private String currentPhotoPath;

    private SharedPreferences user_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        try {
            getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button confirm_reg = findViewById(R.id.button);

        confirm_reg.setOnClickListener(e -> {
            if(checkFields() == OK_CHECK){
                //returns instance pointing to the file that contains values to be saved
                //MODE_PRIVATE: the file can only be accessed using calling application
                user_data = getSharedPreferences(MyPREF, MODE_PRIVATE);

                SharedPreferences.Editor editor = user_data.edit();

                //store data into file
                editor.putString(Name, name);
                editor.putString(Surname, surname);
                editor.putString(Address, addr);
                editor.putString(Description, desc);
                editor.putString(Password, psw);
                editor.putString(Email, mail);
                editor.putString(Phone, phone);
                editor.putString(Photo, currentPhotoPath);
                editor.apply();
                //data saved and start new activity
                Intent i = new Intent();
                i.putExtra(Name, name);
                i.putExtra(Surname, surname);
                i.putExtra(Address, addr);
                i.putExtra(Description, desc);
                i.putExtra(Email, mail);
                i.putExtra(Phone, phone);
                i.putExtra(Photo, currentPhotoPath);
                setResult(1, i);
                finish();
            }
            else{
                AlertDialog alertDialog = new AlertDialog.Builder(EditProfile.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Alert message to be shown");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        ImageView img = findViewById(R.id.imageView2);
        img.setOnClickListener(e -> {
            AlertDialog alertDialog = new AlertDialog.Builder(EditProfile.this, R.style.AlertDialogStyle).create();
            LayoutInflater factory = LayoutInflater.from(EditProfile.this);
            final View view = factory.inflate(R.layout.custom_dialog, null);
            alertDialog.setView(view);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Camera",
                    (dialog, which) -> {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                // Error occurred while creating the File
                                Log.d("my tag","Erorre nel creare file.");
                            }
                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(this,
                                        "com.example.android.fileprovider",
                                        photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(takePictureIntent, 2);
                            }
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Gallery",
                    (dialog, which) -> {
                        if (ContextCompat.checkSelfPermission(this,
                                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    1);
                            Log.d("Permission Run Time", "Obtained");
                        }
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, 1);
                    });
            alertDialog.show();
        });
    }

    private int checkFields(){
        name = ((EditText)findViewById(R.id.name)).getText().toString();
        surname = ((EditText)findViewById(R.id.surname)).getText().toString();
        addr = ((EditText)findViewById(R.id.address)).getText().toString();
        desc = ((EditText)findViewById(R.id.description)).getText().toString();
        psw = ((EditText)findViewById(R.id.password)).getText().toString();
        mail = ((EditText)findViewById(R.id.mail)).getText().toString();
        phone = ((EditText)findViewById(R.id.phone)).getText().toString();

        if(name.trim().length() == 0)
            return NAME_ERROR;

        if(surname.trim().length() == 0)
            return SURNAME_ERROR;

        if(addr.trim().length() == 0)
            return ADDR_ERROR;

        if(psw.trim().length() == 0)
            return PSW_ERROR;

        if(mail.trim().length() == 0 || !android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches())
            return MAIL_ERROR;

        if(phone.trim().length() == 0)
            return PHONE_ERROR;

        return OK_CHECK;
    }

    private void getData() throws IOException {
        user_data = getSharedPreferences(MyPREF, MODE_PRIVATE);

        String nm = user_data.getString(Name, "");
        String surnm = user_data.getString(Surname, "");
        String addr = user_data.getString(Address, "");
        String desc = user_data.getString(Description, "");
        String psw = user_data.getString(Password, "");
        String email = user_data.getString(Email, "");
        String phone = user_data.getString(Phone, "");
        String photoPath = user_data.getString(Photo, "");

        ((EditText)findViewById(R.id.name)).setText(nm);
        ((EditText)findViewById(R.id.surname)).setText(surnm);
        ((EditText)findViewById(R.id.address)).setText(addr);
        ((EditText)findViewById(R.id.description)).setText(desc);
        ((EditText)findViewById(R.id.password)).setText(psw);
        ((EditText)findViewById(R.id.mail)).setText(email);
        ((EditText)findViewById(R.id.phone)).setText(phone);

        File imgFile = new File(photoPath);
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        myBitmap = adjustPhoto(myBitmap, photoPath);
        ((ImageView)findViewById(R.id.imageView2)).setImageBitmap(myBitmap);
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

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File( storageDir + File.separator +
                imageFileName + /* prefix */
                ".jpg"
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && null != data){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            //Log.d("Camera path:", picturePath);
            currentPhotoPath = picturePath;
        }

        if((requestCode == 1 || requestCode == 2) && resultCode == RESULT_OK){
            File imgFile = new File(currentPhotoPath);
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            try {
                myBitmap = adjustPhoto(myBitmap, currentPhotoPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((ImageView)findViewById(R.id.imageView2)).setImageBitmap(myBitmap);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }
}

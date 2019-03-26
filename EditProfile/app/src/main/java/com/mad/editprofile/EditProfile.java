package com.mad.editprofile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    private String name;
    private String surname;
    private String addr;
    private String desc;
    private String psw;
    private String mail;
    private String phone;

    private SharedPreferences user_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Button confirm_reg = findViewById(R.id.button);

        confirm_reg.setOnClickListener(e -> {
            if(checkFields() == 0){
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
                editor.apply();
                //data saved and start new activity
                Intent i = new Intent();
                i.putExtra(Name, name);
                i.putExtra(Surname, surname);
                i.putExtra(Address, addr);
                i.putExtra(Description, desc);
                i.putExtra(Email, mail);
                i.putExtra(Phone, phone);
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
    }

    private int checkFields(){
        //String name = ((TextView)findViewById(R.id.name)).getText().toString();
        //String surname = ((TextView)findViewById(R.id.surname)).getText().toString();
        //String addr = ((TextView)findViewById(R.id.address)).getText().toString();
        //String psw = ((TextView)findViewById(R.id.password)).getText().toString();
        //String mail = ((TextView)findViewById(R.id.mail)).getText().toString();
        //String phone = ((TextView)findViewById(R.id.phone)).getText().toString();

        EditText et_name = findViewById(R.id.name);
        EditText et_surname = findViewById(R.id.surname);
        EditText et_addr = findViewById(R.id.address);
        EditText et_desc = findViewById(R.id.description);
        EditText et_psw = findViewById(R.id.password);
        EditText et_mail = findViewById(R.id.mail);
        EditText et_phone = findViewById(R.id.phone);

        name = et_name.getText().toString();
        surname = et_surname.getText().toString();
        addr = et_addr.getText().toString();
        desc = et_desc.getText().toString();
        psw = et_psw.getText().toString();
        mail = et_mail.getText().toString();
        phone = et_phone.getText().toString();

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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }
}

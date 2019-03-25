package com.mad.editprofile;

import android.content.DialogInterface;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class EditProfile extends AppCompatActivity {
    private static final int OK_CHECK = 0;
    private static final int NAME_ERROR = -1;
    private static final int SURNAME_ERROR = -2;
    private static final int ADDR_ERROR = -3;
    private static final int PSW_ERROR = -4;
    private static final int MAIL_ERROR = -5;
    private static final int PHONE_ERROR = -6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Button confirm_reg = findViewById(R.id.button);

        confirm_reg.setOnClickListener(e -> {
            if(checkFields() == 0){
                //save state -> shared preferences
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

            finish();
        });
    }

    private int checkFields(){
        String name = ((TextView)findViewById(R.id.name)).getText().toString();
        String surname = ((TextView)findViewById(R.id.surname)).getText().toString();
        String addr = ((TextView)findViewById(R.id.address)).getText().toString();
        String psw = ((TextView)findViewById(R.id.password)).getText().toString();
        String mail = ((TextView)findViewById(R.id.mail)).getText().toString();
        String phone = ((TextView)findViewById(R.id.phone)).getText().toString();

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

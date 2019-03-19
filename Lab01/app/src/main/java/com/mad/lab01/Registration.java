package com.mad.lab01;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Registration extends AppCompatActivity {

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
        setContentView(R.layout.activity_registration);

        Button confirm_reg = findViewById(R.id.button);

        confirm_reg.setOnClickListener(e -> {
            if(checkFields() == 0){
                //save state and then finish
                finish();
            }
            else{
                AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
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
        EditText et_name = findViewById(R.id.name);
        EditText et_surname = findViewById(R.id.surname);
        EditText et_addr = findViewById(R.id.address);
        EditText et_psw = findViewById(R.id.password);
        EditText et_mail = findViewById(R.id.mail);
        EditText et_phone = findViewById(R.id.phone);

        String name = et_name.getText().toString();
        String surname = et_surname.getText().toString();
        String addr = et_addr.getText().toString();
        String psw = et_psw.getText().toString();
        String mail = et_mail.getText().toString();
        String phone = et_phone.getText().toString();

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

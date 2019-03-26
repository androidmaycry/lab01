package com.mad.editprofile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;



public class MainActivity extends AppCompatActivity {
    private static final String MyPREF = "User_Data";

    private TextView et_name;
    private TextView et_surname;
    private TextView et_addr;
    private TextView et_desc;
    private TextView et_mail;
    private TextView et_phone;

    private static final String Name = "keyName";
    private static final String Surname = "keySurname";
    private static final String Address = "keyAddress";
    private static final String Description = "keyDescription";
    private static final String Email = "keyEmail";
    private static final String Phone = "keyPhone";

    private SharedPreferences user_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File f = new File(
                "/data/data/com.mad.editprofile/shared_prefs/User_Data.xml");
        if (!f.exists()){
            Intent edit_profile = new Intent(getApplicationContext(), EditProfile.class);
            startActivityForResult(edit_profile, 0);

        }

        et_name = findViewById(R.id.name);
        et_surname = findViewById(R.id.surname);
        et_addr = findViewById(R.id.address);
        et_desc = findViewById(R.id.description);
        et_mail = findViewById(R.id.mail);
        et_phone = findViewById(R.id.phone);

        user_data = getSharedPreferences(MyPREF, MODE_PRIVATE);

        String nm = user_data.getString(Name, "");
        String surnm = user_data.getString(Surname, "");
        String addr = user_data.getString(Address, "");
        String desc = user_data.getString(Description, "");
        String email = user_data.getString(Email, "");
        String phone = user_data.getString(Phone, "");

        et_name.setText(nm);
        et_surname.setText(surnm);
        et_addr.setText(addr);
        et_desc.setText(desc);
        et_mail.setText(email);
        et_phone.setText(phone);

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
            et_name = findViewById(R.id.name);
            et_surname = findViewById(R.id.surname);
            et_addr = findViewById(R.id.address);
            et_desc = findViewById(R.id.description);
            et_mail = findViewById(R.id.mail);
            et_phone = findViewById(R.id.phone);

            String nm = data.getStringExtra(Name);
            String surnm = data.getStringExtra(Surname);
            String addr = data.getStringExtra(Address);
            String desc = data.getStringExtra(Description);
            String email = data.getStringExtra(Email);
            String phone = data.getStringExtra(Phone);

            et_name.setText(nm);
            et_surname.setText(surnm);
            et_addr.setText(addr);
            et_desc.setText(desc);
            et_mail.setText(email);
            et_phone.setText(phone);
        }
    }

    /*@Override
    protected void onRestart() {
        super.onRestart();


        et_name = findViewById(R.id.name);
        et_surname = findViewById(R.id.surname);
        et_addr = findViewById(R.id.address);
        et_desc = findViewById(R.id.description);
        et_mail = findViewById(R.id.mail);
        et_phone = findViewById(R.id.phone);

        Intent i = getIntent();
        String nm = i.getStringExtra(Name);
        String surnm = i.getStringExtra(Surname);
        String addr = i.getStringExtra(Address);
        String desc = i.getStringExtra(Description);
        String email = i.getStringExtra(Email);
        String phone = i.getStringExtra(Phone);

        et_name.setText(nm);
        et_surname.setText(surnm);
        et_addr.setText(addr);
        et_desc.setText(desc);
        et_mail.setText(email);
        et_phone.setText(phone);
    }*/
}

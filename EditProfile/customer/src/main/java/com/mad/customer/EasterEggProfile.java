package com.mad.customer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class EasterEggProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easter_egg);

        ((TextView)findViewById(R.id.name_ee)).setText("Giovanni");
        ((TextView)findViewById(R.id.surname_ee)).setText("Malnati");
        ((TextView)findViewById(R.id.address_ee)).setText("Gujarati Hindu Modh Baniya");
        ((TextView)findViewById(R.id.description_ee)).setText("Malnati for President");
        ((TextView)findViewById(R.id.mail_ee)).setText("bestProf@polito.it");
        ((TextView)findViewById(R.id.phone_ee)).setText("0110907168");
        ((ImageView)findViewById(R.id.profile_image_ee)).setImageResource(R.drawable.photo_malnati);
    }
}
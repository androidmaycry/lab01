package com.mad.lab01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b_signup = findViewById(R.id.signup_button);

        b_signup.setOnClickListener(e -> {
            Intent reg = new Intent(getApplicationContext(), Registration.class);
            startActivity(reg);
        });
    }
}

package com.example.multitranscribe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {

    ImageButton button,button1,button2,button3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        button = findViewById(R.id.ttsbtn);
        button1 = findViewById(R.id.sttbtn);
        button2 = findViewById(R.id.rpbtn);
        button3 = findViewById(R.id.ittbtn);
        //------------------------
        button.setOnClickListener(v -> {
            Intent intent= new Intent(HomeActivity.this,MainActivity2.class);
            startActivity(intent);
        });
        //-----------------------------
        button1.setOnClickListener(v -> {
            Intent intent1= new Intent(HomeActivity.this,MainActivity3.class);
            startActivity(intent1);
        });
        //------------------------
        button2.setOnClickListener(v -> {
            Intent intent2 = new Intent(HomeActivity.this,MainActivity4.class);
            startActivity(intent2);
        });
        //-----------------------------------
        button3.setOnClickListener(v -> {
            Intent intent3 = new Intent(HomeActivity.this,MainActivity5.class);
            startActivity(intent3);
        });
    }


}

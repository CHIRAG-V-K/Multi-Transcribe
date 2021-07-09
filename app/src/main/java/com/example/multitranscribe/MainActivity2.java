package com.example.multitranscribe;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    //global declaration
    EditText mTextEt;
    Button mSpeakBtn, mStopBtn;

    //views
    TextToSpeech mTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //subtitle
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setSubtitle("Choose language to speech");

        //initialization
        mTextEt = findViewById(R.id.textEt);
        mSpeakBtn = findViewById(R.id.speakBtn);
        mStopBtn = findViewById(R.id.stopBtn);

        mTTS = new android.speech.tts.TextToSpeech( getApplicationContext(), status -> {
            if(status!= android.speech.tts.TextToSpeech.ERROR){
                // If there is no error in the text to speech library
                mTTS.setLanguage(Locale.UK);
                Toast.makeText(this, "Default language is set to English", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity2.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        //speaking button
        mSpeakBtn.setOnClickListener(v -> {
            //get text from edit text
            String toSpeak = mTextEt.getText().toString().trim();
            if (toSpeak.equals("")){
                //if it is empty
                Toast.makeText(MainActivity2.this, "Please Enter Text...", Toast.LENGTH_SHORT).show();
            }
            else {
                //Toast.makeText(MainActivity2.this, toSpeak, Toast.LENGTH_SHORT).show();
                //reads the text
                mTTS.speak(toSpeak, android.speech.tts.TextToSpeech.QUEUE_FLUSH,null);
            }
        });

        mStopBtn.setOnClickListener(v -> {
            if (mTTS.isSpeaking()){
                mTTS.stop();
//                mTTS.shutdown();
            }
            else {
                Toast.makeText(MainActivity2.this, "I am Not Speaking", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onPause() {
        if (mTTS != null){
            mTTS.stop();
        }
        super.onPause();
    }

    //---------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.text_to_speech_menu, menu);
        return true;
    }

    //handle actionbar item clicks
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            showLanguageImportDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showLanguageImportDialog() {
        //items to display in dialog
        String[] items = {"English", "Hindi"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        //set Title
        dialog.setTitle("Select Language");
        dialog.setItems(items, (dialog1, which) -> {
            if (which == 0) {
               //if choosen language is english
                mTTS.setLanguage(Locale.UK);

            }
            if (which == 1) {
                //if choosen langauge is hindi
                mTTS.setLanguage(Locale.forLanguageTag("hin"));
            }
        });
        dialog.create().show();//show dialog
    }

}

package com.example.multitranscribe;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class MainActivity4 extends AppCompatActivity {
    TextView moutput,mtextview, mnum;
    Button mSpeakBtn, mStopBtn;
    FloatingActionButton mfab;
    InputStream ips;
    String fullPath;
    int NUMBEROFPAGES;



    TextToSpeech mTTS;

    private static final int THECODE = 69;
    private static final String PRIMARY = "primary";
    private static final String LOCAL_STORAGE = "/storage/self/primary/";
    private static final String EXT_STORAGE = "/storage/1217-3C18/";
//------------------------------------------------------------------------------------------------------------------------------------
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        //initialization
        moutput = findViewById(R.id.output_text);
        mSpeakBtn = findViewById(R.id.speakBtn);
        mStopBtn = findViewById(R.id.stopBtn);
        mfab = findViewById(R.id.fab);
        mtextview = findViewById(R.id.filepath);
        mnum = findViewById(R.id.Num);


        mTTS = new TextToSpeech(getApplicationContext(), status ->{
            if(status!= TextToSpeech.ERROR){
                // If there is no error in the text to speech library
                mTTS.setLanguage(Locale.US);
            }
            else {
                Toast.makeText(MainActivity4.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        mSpeakBtn.setOnClickListener(v -> {
            //get text from edit text
            String toSpeak = moutput.getText().toString().trim();
            if (toSpeak.equals("")){
                //if it is empty
                Toast.makeText(MainActivity4.this, "Please Enter Text...", Toast.LENGTH_SHORT).show();
            }
            else {
//                Toast.makeText(MainActivity4.this, toSpeak, Toast.LENGTH_SHORT).show();
                //reads the text
                mTTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH,null);
            }
        });

        mStopBtn.setOnClickListener(v -> {
            if (mTTS.isSpeaking()){
                mTTS.stop();
//                mTTS.shutdown();
            }
            else {
                Toast.makeText(MainActivity4.this, "I am Not Speaking", Toast.LENGTH_SHORT).show();
            }
        });

        //----------------fab button------------
        mfab.setOnClickListener(v -> {
            moutput.setText("");
            mtextview.setText("");
            selectFileFromStorage();
        });

    }
//--------------------------------------------------------------------------------------------------------------------------------
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void selectFileFromStorage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent,THECODE);
//        NUMBEROFPAGES = Integer.parseInt(mnum.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==THECODE && resultCode== Activity.RESULT_OK){
            if (data!= null){
                Uri uri = data.getData();
                if (uri.getPath().contains(PRIMARY)){
                    fullPath = LOCAL_STORAGE + uri.getPath().split(":")[1];
                }
                else {
                    fullPath = EXT_STORAGE + uri.getPath().split(":")[1];
                }
                mtextview.setText(fullPath);
                extractTextFromPdf(uri);
            }
        }else {
            Toast.makeText(this, "Wrong File", Toast.LENGTH_SHORT).show();
        }
    }

    private void extractTextFromPdf(Uri uri) {
        try {
            ips = MainActivity4.this.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            PdfReader reader = new PdfReader(ips);
            int pages = reader.getNumberOfPages();
//            String s = Integer.toString(pages);
//            Log.v("NU",s);
            try {
                NUMBEROFPAGES = Integer.parseInt(mnum.getText().toString());
            }catch (Exception e){
                Toast.makeText(this, "Enter Page Number to READ", Toast.LENGTH_SHORT).show();
                NUMBEROFPAGES = 1;
            }
            if ( NUMBEROFPAGES==pages || NUMBEROFPAGES<pages) {
                String fileContent = "";
                fileContent = PdfTextExtractor.getTextFromPage(reader, NUMBEROFPAGES);
                StringBuilder sb = new StringBuilder();
                sb.append(fileContent);
                reader.close();
//              runOnUiThread(() ->
                moutput.setText(sb.toString());
            } else {
               mTTS.speak("Enter a valid Page Number",TextToSpeech.QUEUE_FLUSH,null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //-------------------------------------------------------
    @Override
    protected void onPause() {
        if (mTTS != null){
            mTTS.stop();
        }
        super.onPause();

    }


}
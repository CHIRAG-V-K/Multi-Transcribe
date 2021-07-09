package com.example.multitranscribe;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity3 extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    //views
    EditText mTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        //subtitle
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setSubtitle("You can Transfer this to PDF");

        mTV = findViewById(R.id.Et);

    }
//--------------------------------------------------------------
    private void speak() {
        //intent to show dialog box of google
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"hi speak something");

        try {
            startActivityForResult(intent,REQUEST_CODE_SPEECH_INPUT);
        }
        catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
//-----------------------------------------------------------------


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                //put the result in the text view
                mTV.append(result.get(0)+" ");

            }
        } else {
            throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }
//------------------------------------------------------------------
//---------------------------------------------------------------------------
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    //inflate menu
    getMenuInflater().inflate(R.menu.speech_to_text_menu, menu);
    return true;
}

    //handle actionbar item clicks
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id== R.id.voiceBtn){
            speak();
        }
        if (id == R.id.save) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
                {
                    String[] permission={Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permission,200);
                }
                else savePdf();
            }else savePdf();
        }
        if (id == R.id.view){
            Intent intent =new Intent(getApplicationContext(),DisplayActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void savePdf() {
        String mfile=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        String mfilepath= getExternalFilesDir(null).toString()+"/"+mfile+".pdf";
        File file =new File(mfilepath);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Document doc=new Document(PageSize.A4);
        //pdfwriter class of itext jar package is useed here
        try {
            PdfWriter.getInstance(doc,new FileOutputStream(file.getAbsoluteFile()));
        } catch (DocumentException e) {
            Toast.makeText(this,"Documentation Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            Toast.makeText(this,"FileNotFound Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        doc.open();
        String mtext = mTV.getText().toString();
        Font smallBold=new Font(Font.FontFamily.TIMES_ROMAN,20,Font.NORMAL);
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Paragraph(mtext,smallBold));
        try {
            doc.add(paragraph);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        doc.close();
        PdfPath.pdfPath = mfilepath;
        Toast.makeText(this, ""+mfile+".pdf"+" is saved to "+mfilepath, Toast.LENGTH_LONG).show();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                savePdf();
            } else Toast.makeText(this, "permission denied..", Toast.LENGTH_SHORT).show();
        }
    }

}

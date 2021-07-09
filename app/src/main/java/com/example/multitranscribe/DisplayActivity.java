package com.example.multitranscribe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DisplayActivity extends AppCompatActivity {
    PDFView pdfView;
    TextView tv ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        tv = findViewById(R.id.TV);
        pdfView = findViewById(R.id.viewpdf);

        File file = new File(PdfPath.pdfPath);
        tv.setText(PdfPath.pdfPath);
        pdfView.fromFile(file).swipeHorizontal(false).defaultPage(0).enableAnnotationRendering(false).enableDoubletap(true)
                .password(null).scrollHandle(null).load();


    }
}
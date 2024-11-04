package com.mtechsoft.bookapp.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.ScrollView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.scroll.ScrollHandle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AppExt {
    private static final String PDF_FILE_NAME = "button.pdf";

    public static String getFilePath(Context context , String chapter_name) {
//        File f = new File(context.getCacheDir() + "/" + PDF_FILE_NAME);
//        if (!f.exists()) try {
//            InputStream is = context.getAssets().open(PDF_FILE_NAME);
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//
//            FileOutputStream fos = new FileOutputStream(f);
//            fos.write(buffer);
//            fos.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        return f.getPath();


            File pdfFile = new File(Environment.getExternalStorageDirectory() + "/bookapp/" + chapter_name+".pdf");  // -> filename = maven.pdf
//            Uri path = Uri.fromFile(pdfFile);
//            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
//            pdfIntent.setDataAndType(path, "application/pdf");
//            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);Toa
            return pdfFile.getPath();
//            try{
//                context.startActivity(pdfIntent);
//            }catch(ActivityNotFoundException e){
//
//                Toast.makeText(context, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
//            }

    }

    public static  void showPDFFile(PDFView pdfView, OnPageChangeListener listener, File chap_url) {
//        public static  void showPDFFile(PDFView pdfView, File chap_url) {
//        Uri myUri = Uri.parse(chap_url);


        pdfView.fromUri(Uri.fromFile(new File(String.valueOf(chap_url))))
////        pdfView.fromAsset("button.pdf")


//        pdfView.fromUri(Uri.fromFile(new File(String.valueOf(chap_url))))
                .defaultPage(0)
                .enableSwipe(true) // allows to block changing pages using swipe
                .onPageChange(listener)
                .swipeHorizontal(false)
                .spacing(10) // in dp
                .enableAnnotationRendering(true)
                .load();

    }
}

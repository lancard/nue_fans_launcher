package com.github.lancard.nue_fans_launcher;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class WebAppInterface {
    Context context;

    public WebAppInterface(Context _context) {
        context = _context;
    }

    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(context, toast, Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public void appendFile(String message) {
        try {
            String messageWithCRLF = message + "\r\n";

            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            Files.write(Paths.get(root.getAbsolutePath() + "/nue-fans-log.txt"), messageWithCRLF.getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            Log.e("main", e.toString());
        }
    }
}

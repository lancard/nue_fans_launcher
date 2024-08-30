package com.github.lancard.nue_fans_launcher;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
    Context context;

    public WebAppInterface(Context _context) {
        context = _context;
    }

    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(context, toast, Toast.LENGTH_LONG).show();
    }
}

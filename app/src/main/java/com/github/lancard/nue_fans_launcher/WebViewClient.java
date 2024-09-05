package com.github.lancard.nue_fans_launcher;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.Toast;

public class WebViewClient extends android.webkit.WebViewClient {
    MainActivity context;

    public WebViewClient(MainActivity _context) {
        context = _context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Log.d("main", request.getUrl().toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(request.getUrl().toString()));
        context.startActivity(intent);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        Log.d("main", "nue fans launched!");
        Toast.makeText(context, "nue fans launched!", Toast.LENGTH_SHORT).show();
    }
}

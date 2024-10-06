package com.github.lancard.nue_fans_launcher;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

public class WebViewClient extends android.webkit.WebViewClient {
    MainActivity context;

    public WebViewClient(MainActivity _context) {
        context = _context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        if (url.startsWith("file://")) {
            return false;
        }
        Log.d("main", request.getUrl().toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(request.getUrl().toString()));
        context.startActivity(intent);
        return true;
    }
}

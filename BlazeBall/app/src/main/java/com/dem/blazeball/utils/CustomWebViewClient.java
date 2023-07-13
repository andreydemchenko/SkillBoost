package com.dem.blazeball.utils;

import android.webkit.WebView;
import android.webkit.WebViewClient;


public class CustomWebViewClient extends WebViewClient {

    @Override
    public void onPageFinished(WebView view, String url) {
        onPageFinishedNative(view, url);
    }

    private native void onPageFinishedNative(WebView view, String url);
}

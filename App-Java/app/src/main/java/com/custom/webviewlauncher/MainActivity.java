// Beginning of MainActivity.java

package com.custom.webviewlauncher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowInsets;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.widget.Button;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
// Declare necessary variables
private GestureDetector gestureDetector;
private static CustomWebView webview;
private static String url;
private static final String PREFS_NAME = "customwebviewlauncher";
private static final String URL_KEY = "url";
private Button settingsButton;

// Method to update the URL and load a new page
public static void setUrl(String newUrl) {
    url = newUrl;
    saveUrlToPrefs(url);
    webview.loadUrl("file:///android_asset/custom_error_page.html?url=" + url);
}

// onCreate method - called when the activity is created
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Load the URL from SharedPreferences, or use the default URL
    SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    url = prefs.getString(URL_KEY, "http://192.168.17.10");

    // Hide navigation bar based on Android version
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        getWindow().getDecorView().getWindowInsetsController().hide(
                WindowInsets.Type.statusBars()
                        | WindowInsets.Type.navigationBars()
        );
    } else {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    // Set up settings button and make it initially invisible
    settingsButton = findViewById(R.id.settings_button);
    settingsButton.setVisibility(View.GONE);

    // Initialize GestureDetector with the custom listener
    gestureDetector = new GestureDetector(this, new SwipeUpGestureListener());

    // Set up WebView, enable JavaScript, add JavaScriptInterface, and load the initial URL
    webview = findViewById(R.id.webview);
    webview.setGestureDetector(gestureDetector);
    webview.setWebViewClient(new RetryWebViewClient());
    webview.getSettings().setJavaScriptEnabled(true);
    webview.getSettings().setDomStorageEnabled(true);
    // Disable cache
    // webview.getSettings().setAppCacheEnabled(false); // Deprecated
    webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    webview.addJavascriptInterface(new JavaScriptInterface(this), "Android");
    webview.loadUrl(url);

    // Set up settings button click listener
    settingsButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, PasswordActivity.class);
            startActivity(intent);
        }
    });
}

// Override the onTouchEvent method to use GestureDetector
@Override
public boolean onTouchEvent(MotionEvent event) {
    gestureDetector.onTouchEvent(event);
    return super.onTouchEvent(event);
}

// Save the URL to SharedPreferences
private static void saveUrlToPrefs(String url) {
    // Save the URL to SharedPreferences
    SharedPreferences.Editor editor = webview.getContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
    editor.putString(URL_KEY, url);
    editor.apply();
}

// Get the current URL
public static String getUrl() {
    return url;
}

// Custom gesture listener for detecting swipe up gestures
public class SwipeUpGestureListener extends GestureDetector.SimpleOnGestureListener {
    // Vertical minimum pixel size of the swipe
    private static final int SWIPE_THRESHOLD = 500;
    // Minimum velocity of the swipe
    private static final int SWIPE_VELOCITY_THRESHOLD = 300;
    // Duration of button visibility after swipe
    private static final int BUTTON_VISIBLE_DURATION = 2000; // 2 seconds

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float diffY = e2.getY() - e1.getY();

        // Detect up swipe from the bottom 25% of the WebView
        if (e1.getY() > (webview.getHeight() * 0.75) && Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
            if (diffY < 0) {
                // Up swipe
                settingsButton.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        settingsButton.setVisibility(View.GONE);
                    }
                }, BUTTON_VISIBLE_DURATION);
                return true;
            }
        }
        return false;
    }
}

// Custom WebViewClient to handle error pages
public static class RetryWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        // Always load the requested URL within the WebView
        view.loadUrl(request.getUrl().toString());
        // Returning true prevents rendering the default "webpage not found" in all cases
        return true;
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        view.loadUrl("file:///android_asset/custom_error_page.html?url=" + url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }
}

// JavaScriptInterface for communicating with the WebView
public class JavaScriptInterface {
    Context mContext;

    JavaScriptInterface(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public void onConnectionStatusChanged(boolean connectionStatus) {

    }
}
}
// End of MainActivity.java



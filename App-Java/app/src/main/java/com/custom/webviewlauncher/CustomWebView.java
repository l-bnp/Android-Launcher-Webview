// Beginning of CustomWebView.java

package com.custom.webviewlauncher;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebView;

public class CustomWebView extends WebView {

    private GestureDetector gestureDetector;

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // Set the GestureDetector for the WebView
    public void setGestureDetector(GestureDetector gestureDetector) {
        this.gestureDetector = gestureDetector;
    }

    // Override the onTouchEvent method to include the GestureDetector
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector != null) {
            gestureDetector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }
}

// End of CustomWebView.java
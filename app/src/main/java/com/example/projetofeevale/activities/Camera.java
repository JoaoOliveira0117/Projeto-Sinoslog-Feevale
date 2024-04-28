package com.example.projetofeevale.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.projetofeevale.databinding.ActivityCameraBinding;

import java.util.Objects;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Camera extends AppCompatActivity {

    private TextView fullscreenContent;
    private LinearLayout fullscreenContentControls;
    private final Handler hideHandler = new Handler(Objects.requireNonNull(Looper.myLooper()));

    @SuppressLint("InlinedApi")
    private final Runnable hidePart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            if (Build.VERSION.SDK_INT >= 30) {
                Objects.requireNonNull(fullscreenContent.getWindowInsetsController()).hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            } else {
                fullscreenContent.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LOW_PROFILE |
                                View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }
    };

    private final Runnable showPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            //Objects.requireNonNull(getSupportActionBar()).show();
            fullscreenContentControls.setVisibility(View.VISIBLE);
        }
    };
    private boolean isFullscreen = false;

    private final Runnable hideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener delayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.projetofeevale.databinding.ActivityCameraBinding binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        isFullscreen = true;

        // Set up the user interaction to manually show or hide the system UI.
        fullscreenContent = binding.fullscreenContent;
        fullscreenContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        fullscreenContentControls = binding.fullscreenContentControls;

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        binding.dummyButton.setOnTouchListener(delayHideTouchListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (isFullscreen) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        //Objects.requireNonNull(getSupportActionBar()).hide();
        fullscreenContentControls.setVisibility(View.GONE);
        isFullscreen = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable);
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        if (Build.VERSION.SDK_INT >= 30) {
            Objects.requireNonNull(fullscreenContent.getWindowInsetsController()).show(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        } else {
            fullscreenContent.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        isFullscreen = true;

        // Schedule a runnable to display UI elements after a delay
        hideHandler.removeCallbacks(hidePart2Runnable);
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        hideHandler.removeCallbacks(hideRunnable);
        hideHandler.postDelayed(hideRunnable, delayMillis);
    }

    private static final boolean AUTO_HIDE = true;

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    private static final int UI_ANIMATION_DELAY = 300;
}

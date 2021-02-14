package com.example.glowchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import java.io.File;

public class MediaDetailActivity extends AppCompatActivity {

    ImageView media_detail;

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    //private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_detail);

        // image view to display the image
        media_detail = findViewById(R.id.mediaDetailImage);
        // the image will be zoomed in/out depending upon
        // by pinching in/out
        // the object detects the pinching gesture
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        // get the image file
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File file = wrapper.getDir("IMAGES",MODE_PRIVATE);
        File[] files = file.listFiles();
        // get the image bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(files[0].getPath());
        media_detail.setImageBitmap(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mScaleGestureDetector.onTouchEvent(event);

        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));
            media_detail.setScaleX(mScaleFactor);
            media_detail.setScaleY(mScaleFactor);
            return true;
        }
    }
}
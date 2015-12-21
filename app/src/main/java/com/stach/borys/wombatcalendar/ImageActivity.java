package com.stach.borys.wombatcalendar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageActivity extends AppCompatActivity {

    ImageView mImageView;
    PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Intent intent = getIntent();
        String image = intent.getStringExtra(WeekActivity.PICTURE_INTENT) + "_full";

        mImageView = (ImageView) findViewById(R.id.iv_photo);
        Drawable bitmap = ContextCompat.getDrawable(this, getResources().getIdentifier(image, "drawable", getPackageName()));
        mImageView.setImageDrawable(bitmap);

        mAttacher = new PhotoViewAttacher(mImageView);
    }
}

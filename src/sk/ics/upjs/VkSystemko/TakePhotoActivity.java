package sk.ics.upjs.VkSystemko;

import android.app.Activity;
import android.graphics.Bitmap;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.ecotastic.android.camerautil.lib.CameraIntentHelperActivity;
import de.ecotastic.android.camerautil.util.BitmapHelper;

/**
 * Created by Viliam on 4.6.2014.
 */
public class TakePhotoActivity extends CameraIntentHelperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
    }

    public void onStartCamera(View view) {
        startCameraIntent();
    }

    @Override
    protected void onPhotoUriFound() {
        TextView uirView = (TextView) findViewById(R.id.acitvity_take_photo_image_uri);
        uirView.setText("photo uri: " + photoUri.toString());

        Bitmap photo = BitmapHelper.readBitmap(this, photoUri);
        if (photo != null) {
            //photo = BitmapHelper.shrinkBitmap(photo, 300, rotateXDegrees);
            //ImageView imageView = (ImageView) findViewById(R.id.acitvity_take_photo_image_view);
            //imageView.setImageBitmap(photo);
            ImageView imageView = (ImageView) findViewById(R.id.trackImageView);
            imageView.setImageBitmap(photo);
          //  RelativeLayout layout = (RelativeLayout) findViewById(R.id.playerLayout);
           // layout.setBackground(new BitmapDrawable(photo));
        }

        //Delete photo in second location (if applicable)
        if (preDefinedCameraUri != null && !preDefinedCameraUri.equals(photoUri)) {
            BitmapHelper.deleteImageWithUriIfExists(preDefinedCameraUri, this);
        }
        //Delete photo in thid location (if applicable)
        if (photoUriIn3rdLocation != null) {
            BitmapHelper.deleteImageWithUriIfExists(photoUriIn3rdLocation, this);
        }
    }

    @Override
    protected void onPhotoUriNotFound() {
        super.onPhotoUriNotFound();
        TextView uirView = (TextView) findViewById(R.id.acitvity_take_photo_image_uri);
        uirView.setText("photo uri: not found");
    }
}





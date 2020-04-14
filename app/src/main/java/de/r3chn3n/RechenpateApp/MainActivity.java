package de.r3chn3n.RechenpateApp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private ImageView viewImage;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
    boolean isFABOpen = false;
    FloatingActionButton bAddPicture;
    FloatingActionButton bDelete;
    FloatingActionButton bTakePicture;
    FloatingActionButton bGallery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bAddPicture = findViewById(R.id.btnAddPhoto);
        bDelete = findViewById(R.id.btnDeletePhoto);
        bTakePicture = findViewById(R.id.btnTakePhoto);
        bGallery = findViewById(R.id.btnSelectPhoto);
        viewImage = findViewById(R.id.viewImage);
        setOnClickListeners();
    }

    /**
     * close fab menu if you click somewhere else but not the buttons
     * @param event event where you touched
     * @return boolean decides how to route the touch events.
     */
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isFABOpen) {
                Rect outRect = new Rect();
                bGallery.getGlobalVisibleRect(outRect);
                boolean galleryTouched = outRect.contains((int)event.getRawX(), (int) event.getRawY());
                bTakePicture.getGlobalVisibleRect(outRect);
                boolean takePictureTouched = outRect.contains((int)event.getRawX(), (int) event.getRawY());
                bDelete.getGlobalVisibleRect(outRect);
                boolean deleteTouched = outRect.contains((int)event.getRawX(), (int) event.getRawY());
                if (!(galleryTouched || takePictureTouched || deleteTouched)) {
                    closeFABMenu();
                    return true;
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * setup click listeners to all floating action buttons
     */
    private void setOnClickListeners() {
        bAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });

        bTakePicture.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        bGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EasyPermissions.hasPermissions(MainActivity.this, galleryPermissions)) {
                    File imagePath = new File(MainActivity.this.getFilesDir(), "images");
                    File newFile = new File(imagePath, "default_image.jpg");
                    Uri uri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", newFile);
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, GALLERY_REQUEST);
                } else {
                    EasyPermissions.requestPermissions(MainActivity.this, "Access for storage", 101, galleryPermissions);
                }
            }
        });

        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewImage.setImageDrawable(null);
                closeFABMenu();
            }
        });
    }

    private void closeFABMenu() {
        isFABOpen = false;
        bAddPicture.animate().translationY(0);
        bDelete.animate().translationY(0);
        bTakePicture.animate().translationY(0);
        bGallery.animate().translationY(0);
        bTakePicture.setVisibility(View.INVISIBLE);
        bGallery.setVisibility(View.INVISIBLE);
        bDelete.setVisibility(View.INVISIBLE);
    }

    /**
     * method that call submethods to loads picture in screen: 3 options
     * 1. take a picture
     * 2. load a picture from gallery
     */

    private void showFABMenu() {
        isFABOpen=true;
        bAddPicture.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
        bDelete.setVisibility(View.VISIBLE);
        bTakePicture.setVisibility(View.VISIBLE);
        bGallery.setVisibility(View.VISIBLE);
        bDelete.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        bTakePicture.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        bGallery.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
    }


    /**
     * load of pictures to background
     * @param requestCode 1 or 2, 1 = take a picture, 2 = load a picture from gallery
     * @param resultCode = RESULT_OK TODO was bedeutet das?
     * @param data the information of the picture
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bitmap photo;
            if (requestCode == 1) {
                photo = (Bitmap) data.getExtras().get("data");
                viewImage.setImageBitmap(photo);
                bDelete.setVisibility(View.VISIBLE);
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                assert selectedImage != null;
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                assert c != null;
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                photo = (BitmapFactory.decodeFile(picturePath));
                viewImage.setImageBitmap(photo);
                bDelete.setVisibility(View.VISIBLE);
            }
            closeFABMenu();
        }
    }

}



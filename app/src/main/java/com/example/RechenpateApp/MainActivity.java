package com.example.RechenpateApp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.RechenpateApp.R;

import java.io.File;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private ImageView viewImage;
    private Button b;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private Bitmap photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b = findViewById(R.id.btnSelectPhoto);
        viewImage = findViewById(R.id.viewImage);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds options to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * method that call submethods to loads picture in screen: 3 options
     * 1. take a picture
     * 2. load a picture from gallery
     * 3. cancel dialog
     */
    private void selectImage() {
        final CharSequence[] options = {"Foto erstellen", "Aus Galerie wählen", "Abbrechen"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Foto hinzufügen");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Foto erstellen")) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    } else {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                } else if (options[item].equals("Aus Galerie wählen")) {
                    if (EasyPermissions.hasPermissions(MainActivity.this, galleryPermissions)) {
                        File imagePath = new File(MainActivity.this.getFilesDir(), "images");
                        File newFile = new File(imagePath, "default_image.jpg");
                        Uri uri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", newFile);
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(intent, GALLERY_REQUEST);
                    } else {
                        EasyPermissions.requestPermissions(MainActivity.this, "Access for storage", 101, galleryPermissions);
                    }
                } else if (options[item].equals("Abbrechen")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

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
            if (requestCode == 1) {
                photo = (Bitmap) data.getExtras().get("data");
                viewImage.setImageBitmap(photo);
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
            }
        }
    }

}



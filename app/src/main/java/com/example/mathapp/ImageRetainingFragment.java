package com.example.mathapp;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class ImageRetainingFragment extends Fragment {
    private Bitmap selectedImage;
    @Override    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }
    public void setImage(Bitmap selectedImage) {
        this.selectedImage = selectedImage;
    }
    public Bitmap getImage() {
        return this.selectedImage;
    }
}

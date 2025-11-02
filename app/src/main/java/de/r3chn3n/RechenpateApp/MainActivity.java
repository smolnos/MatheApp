package de.r3chn3n.RechenpateApp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private PaintView paintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paintView = findViewById(R.id.paint_view);
        FloatingActionButton btnDeleteCircle = findViewById(R.id.btnDeleteCircle);

        // Mach den Button sichtbar, sobald du willst
        btnDeleteCircle.setVisibility(View.VISIBLE);

        btnDeleteCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintView.deleteCircles();
            }
        });
    }
}

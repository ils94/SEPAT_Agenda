package com.droidev.sepatagenda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class DetalhesActivity extends AppCompatActivity {

    TextView textViewDetalhes;
    String detalhes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        setTitle("Detalhes");

        textViewDetalhes = findViewById(R.id.detalhes);
        textViewDetalhes.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        detalhes = intent.getStringExtra("detalhes");

        textViewDetalhes.setText(detalhes);
    }
}
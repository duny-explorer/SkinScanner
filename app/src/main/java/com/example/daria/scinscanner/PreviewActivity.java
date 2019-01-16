package com.example.daria.scinscanner;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;


public class PreviewActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        ImageView preview = findViewById(R.id.preview);

        Uri uri = Uri.parse(getIntent().getStringExtra("Uri"));
        preview.setImageURI(uri);

        ImageButton btnOK = findViewById(R.id.btnOK);
        ImageButton btnNO = findViewById(R.id.btnNO);

        btnNO.setOnClickListener(this);
        btnOK.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btnNO:
                intent.putExtra("btn", "no");
                setResult(RESULT_OK, intent);
                finish();
            case R.id.btnOK:
                intent.putExtra("btn", "ok");
                setResult(RESULT_OK, intent);
                finish();
        }
    }
}

package com.example.daria.scinscanner;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("");

        ImageButton btnCamera = findViewById(R.id.btnCamera);
        ImageButton btnPicture = findViewById(R.id.btnPicture);
        ImageButton btnInfoDisease = findViewById(R.id.btnInfoDisease);

        btnCamera.setOnClickListener(this);
        btnPicture.setOnClickListener(this);
        btnInfoDisease.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCamera:
                dispatchTakePictureIntent();
                break;
            case R.id.btnPicture:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
                break;
            case R.id.btnInfoDisease:
                startActivity(new Intent(this, ResultActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String uriString = "";
            switch (requestCode) {
                case 1:
                    uriString = uri.toString();
                    break;
                case 2:
                    uriString = data.getData().toString();
                    break;
                case 3:
                    if (data.getStringExtra("btn").equals("ok")) {
                        startActivity(new Intent(this, ResultActivity.class));
                    }
            }

            if (requestCode == 1 || requestCode == 2) {
                Intent intent = new Intent(this, PreviewActivity.class);
                intent.putExtra("Uri", uriString);
                startActivityForResult(intent, 3);
            }
        }
    }

    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);


        //String mCurrentPhotoPath = image.getAbsolutePath();
        return File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Ошибка!", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                uri = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(takePictureIntent, 1);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings_1:
                showDialog(1);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this, R.style.DialogStyle);

            adb.setTitle(R.string.action_settings_1);

            adb.setMessage(R.string.about_program);

            adb.setIcon(android.R.drawable.ic_dialog_info);

            return adb.create();
        }
        return super.onCreateDialog(id);
    }
}

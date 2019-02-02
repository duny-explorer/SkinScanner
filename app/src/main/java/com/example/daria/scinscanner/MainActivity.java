package com.example.daria.scinscanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.theartofdev.edmodo.cropper.CropImage;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Uri uri;
    protected String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("");

        ImageButton btnPicture = findViewById(R.id.btnPicture);
        ImageButton btnInfoDisease = findViewById(R.id.btnInfoDisease);

        btnPicture.setOnClickListener(this);
        btnInfoDisease.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPicture:
                CropImage.startPickImageActivity(this);
                break;
            case R.id.btnInfoDisease:
                startActivity(new Intent(this, HelpActivity.class));
                break;
        }
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK) {
            Intent intent;

            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                Uri imageUri = CropImage.getPickImageResultUri(this, data);

                if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                    uri = imageUri;
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
                } else {
                    startCropImageActivity(imageUri);
                }
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                mCurrentPhotoPath = CropImage.getActivityResult(data).getUri().getPath();

                Log.d("1234567", mCurrentPhotoPath);
                intent = new Intent(this, ResultActivity.class);
                intent.putExtra("filepath", mCurrentPhotoPath);
                startActivity(intent);
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri).start(this);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], int[] grantResults) {
        if (requestCode == CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CropImage.startPickImageActivity(this);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (uri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCropImageActivity(uri);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
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

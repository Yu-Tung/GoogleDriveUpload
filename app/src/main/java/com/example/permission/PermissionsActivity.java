package com.example.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

@TargetApi(Build.VERSION_CODES.M)

public class PermissionsActivity extends AppCompatActivity {
    private static final String EXTRA_PERMISSIONS = "permission";
    public static final int PERMISSIONS_ACCEPT = 0;

    private static final int PERMISSION_REQUEST_CODE = 0;
    private PermissionsChecker permissionsChecker;
    private boolean isRequireCheck;

    public static void startPermissionsForResult(Activity activity, int requestCode, String... permissions) {
        Intent intent = new Intent(activity, PermissionsActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || !getIntent().hasExtra(EXTRA_PERMISSIONS)) {
            throw new RuntimeException("PermissionsActivity need to use a static method startPermissionsForResult to start!");
        }

        permissionsChecker = new PermissionsChecker(this);
        isRequireCheck = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRequireCheck) {
            String[] permissions = getExtraPermissions();
            if (permissionsChecker.missingPermissions(permissions)) {
                requestPermissions(permissions);
            } else {
                allPermissionsAccept();
            }
        } else {
            isRequireCheck = true;
        }
    }

    private String[] getExtraPermissions() {
        return getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
    }

    private void allPermissionsAccept() {
        setResult(PERMISSIONS_ACCEPT);
        finish();
    }

    private void requestPermissions(String... permissions) {
        this.requestPermissions(permissions, PERMISSION_REQUEST_CODE);
    }
}

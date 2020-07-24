package com.example.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public class PermissionsChecker {
    private Context context;

    public PermissionsChecker(Context context){
        this.context = context;
    }


    /**
     * check every input permissions
     *
     * @param permissions needs permissions
     * @return if this application lose permissions return true else return false
     */
    public boolean missingPermissions(String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED)
                    return true;
            }
        }
        return false;
    }
}

package com.example.ericjeffrey.helloworld.tools;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionHelper {
    public final static int REQUEST_CODE_LOCATION = 10;
    public final static int REQUEST_CODE_STORAGE = 11;

    public static final int GRANTED_LOCATION = 20;
    public static final int GRANTED_STORAGE = 21;

    public static final int DENIED_LOCATION = 30;
    public static final int DENIED_STORAGE = 31;

    public final static int FOREVER_DENIED_LOCATION = 40;
    public final static int FOREVER_DENIED_STORAGE = 41;

    private static int GRANTED = PackageManager.PERMISSION_GRANTED;
    private static int DENIED = PackageManager.PERMISSION_DENIED;

    /**
     * 请求指定权限
     * @param activity 请求权限的 activity
     * @param which 权限类型 - 可选值同 REQUEST_CODE_
     * @return true：已授予权限
     *          false：未授予权限
     */
    public static boolean requestPermission(Activity activity, int which){
        switch (which){
            case REQUEST_CODE_LOCATION:
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == GRANTED)
                    return true;
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, which);
                return false;
            case REQUEST_CODE_STORAGE:
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == GRANTED)
                    return true;
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, which);
                return false;
            default:
                break;
        }
        return false;
    }

    /**
     * 检查是否被授予相应权限
     * @param activity 请求权限的 activity
     * @param requestCode 请求码
     * @param permissions 权限组
     * @param grantResults 结果，非空对象但可能为empty
     * @return GRANTED: 权限被授予
     *         权限值 ： 权限未被授予
     */
    public static int isPermissionGranted(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case REQUEST_CODE_LOCATION:
                if (grantResults.length <= 0 || grantResults[0] == DENIED){
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION))
                        return FOREVER_DENIED_LOCATION;
                    return DENIED_LOCATION;
                }
                return GRANTED_LOCATION;
            case REQUEST_CODE_STORAGE:
                if (grantResults.length <= 0 || grantResults[0] == DENIED){
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                        return FOREVER_DENIED_STORAGE;
                    return DENIED_STORAGE;
                }
                return GRANTED_STORAGE;
            default:
                break;
        }
        return GRANTED;
    }
}

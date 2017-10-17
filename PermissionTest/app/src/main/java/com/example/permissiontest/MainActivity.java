package com.example.permissiontest;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //要申请的权限
    private String[] permissions = {Manifest.permission.CALL_PHONE};
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startCall = (Button) findViewById(R.id.btn_startCall);
        startCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
                } else {
                    call();
                }

            }
        });
    }

    private void call() {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:10000"));
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {//判断用户是否点击了不再提醒。（检测该权限是否还可以申请）
                        boolean flag = shouldShowRequestPermissionRationale(permissions[0]);
                        if (!flag) {//用户还是想用我的App的,提示用户去应用设置界面手动开启权限
                            showSettingDialog();
                        } else {
                            Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        call();
                        Toast.makeText(this, "You accepted the permission", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }

    // 提示用户该请求权限的弹出框
    private void showSettingDialog() {
        dialog = new AlertDialog.Builder(this)
                .setTitle("You denied the permission")
                .setMessage("请在设置-权限中，进行权限修改。")
                .setPositiveButton("accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();

                        //根据包名跳转到系统自带的应用程序信息界面(需要参数)
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.fromParts("package", getPackageName(), null));
                        startActivityForResult(intent, 2);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "You denied the permission", Toast.LENGTH_SHORT).show();
                    }
                })
                .setCancelable(false).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //检查该权限是否已经获取
                    int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                    //判断权限是否已经授权
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        //提示用户应该去设置界面手动开启
                        showSettingDialog();
                    } else {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Toast.makeText(this, "You accepted the permission", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}

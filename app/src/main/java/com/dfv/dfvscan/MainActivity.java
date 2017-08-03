package com.dfv.dfvscan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.dfv.dfvscan.zxing.DvcScanActivity;
import com.dfv.dfvscan.zxing.SecondActivity;

import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Button scanBt,devScanBt;
    private static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        scanBt = (Button) findViewById(R.id.scan_bt);
        devScanBt = (Button) findViewById(R.id.devscan_bt);


        List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
        permissionItems.add(new PermissionItem(Manifest.permission.CAMERA, "照相机", R.drawable.	permission_ic_camera));
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储", R.drawable.permission_ic_storage));
        HiPermission.create(MainActivity.this)
                .permissions(permissionItems)
                .filterColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, getTheme()))//图标的颜色
                .animStyle(R.style.PermissionAnimModal)//设置动画
                .style(R.style.PermissionBlueStyle)//主题样式
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        Log.i(TAG, "onClose");
//                        showToast("用户关闭权限申请");
                        Toast.makeText(MainActivity.this,"用户关闭权限申请",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinish() {
//                        showToast("所有权限申请完成");
                        Toast.makeText(MainActivity.this,"所有权限申请完成",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onDeny(String permission, int position) {
                        Log.i(TAG, "onDeny");
                    }

                    @Override
                    public void onGuarantee(String permission, int position) {
                        Log.i(TAG, "onGuarantee");
                    }
                });


        scanBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //第二个参数是需要申请的权限
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    //权限还没有授予，需要在这里写申请权限的代码
                    ActivityCompat.requestPermissions(MainActivity.this, new
                            String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                } else {
                    //权限已经被授予，在这里直接写要执行的相应方法即可

                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    startActivity(intent);
                }
            }
        });

        devScanBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //第二个参数是需要申请的权限
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    //权限还没有授予，需要在这里写申请权限的代码
                    ActivityCompat.requestPermissions(MainActivity.this, new
                            String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                } else {
                    //权限已经被授予，在这里直接写要执行的相应方法即可

                    Intent intent = new Intent(MainActivity.this, DvcScanActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            } else {
                // Permission Denied
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

package com.star.mypermission;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;

    private Button mCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCall = findViewById(R.id.call);
        mCall.setOnClickListener(v -> makeACall());
    }

    private void makeACall() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.CALL_PHONE},
                    REQUEST_CODE);
        } else {

            makeACallWithPermissions();
        }
    }

    private void makeACallWithPermissions() {

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + "10086"));

        try {
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case REQUEST_CODE:

                if ((grantResults.length > 0) &&
                        (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    makeACallWithPermissions();
                } else {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CALL_PHONE)) {

                        new AlertDialog.Builder(this)
                                .setMessage("提示用户为何要开启此权限")
                                .setPositiveButton("知道了", (dialog, which) ->
                                        ActivityCompat
                                                .requestPermissions(this,
                                                new String[] { Manifest.permission.CALL_PHONE},
                                                REQUEST_CODE))
                                .create()
                                .show();

                    } else {

                        new AlertDialog.Builder(this)
                                .setMessage("该功能需要访问电话的权限，不开启将无法正常工作！")
                                .setPositiveButton("确定", (dialog, which) -> {

                                })
                                .create()
                                .show();
                    }
                }

                break;

            default:
                break;

        }
    }
}

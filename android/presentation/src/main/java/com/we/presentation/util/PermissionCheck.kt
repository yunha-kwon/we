package com.we.presentation.util

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission

fun requestPermission(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        TedPermission.create()
            .setDeniedMessage("권한을 허용하지 않으면 앱을 사용할 수 없습니다.")
            .setDeniedCloseButtonText("닫기")
            .setGotoSettingButtonText("설정")
            .setPermissions(Manifest.permission.POST_NOTIFICATIONS)
            .setPermissionListener(object : PermissionListener{
                override fun onPermissionGranted() {
                    //권한 허용

                }
                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    //권한 거부
                }
            })
            .check()
    }
}
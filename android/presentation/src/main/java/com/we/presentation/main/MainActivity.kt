package com.we.presentation.main

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.we.presentation.R
import com.we.presentation.base.BaseActivity
import com.we.presentation.databinding.ActivityMainBinding
import com.we.presentation.main.viewmodel.MainViewModel
import com.we.presentation.util.Page
import com.we.presentation.util.requestPermission
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var navController: NavController
    private val mainViewModel: MainViewModel by viewModels()


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Timber.tag("FCM 링크").d("$intent")

    }

    override fun init() {
        initKakao()
        initDeepLink()
        setNavGraph()
        initBottomNavigation()
        setBottomNavHide()
        getFcmToken()
        requestPermission()
    }

    private fun initKakao(){
        KakaoSdk.init(this, resources.getString(R.string.kakao_app_key))
    }
    private fun initDeepLink() {
        if (Intent.ACTION_VIEW == intent.action) {
            val uri = intent.data
            if (uri != null) {
                Timber.tag("딥링크").d("$uri")
            }
        }
    }

    private fun setNavGraph() {
        val type = intent.getIntExtra("type", 0)
        Timber.tag("로그인 MainActivity").d("타입 $type")
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController
        navController.navInflater.inflate(R.navigation.nav_graph).apply {
            val id = when (type) { // 이체 : 3-> 대표 계좌 있는 경우, 4 -> 대표 계좌 없는 경우
                1 -> {
                    R.id.homeFragment
                }
                2 -> {
                    R.id.guestFragment
                }
                3 -> {
                    R.id.guestFragment
                }
                else -> {
                    R.id.accountFragment
                }

            }
            setStartDestination(id)
            navController.setGraph(this, null)
        }
    }

    private fun initBottomNavigation() { //바텀 네비게이션 초기화

        binding.bottomNavMain.setupWithNavController(navController)
    }

    /** 바텀 네비게이션 숨기는 기능 */
    private fun setBottomNavHide() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val page = Page.fromId(destination.id)
            binding.bottomVisibility = page?.hideBottomNav == true
        }
    }


    private fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Timber.d("FCM : ${token}")
            mainViewModel.postToken(token)
        })
    }
}

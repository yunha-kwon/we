package com.we.presentation.sign

import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.we.presentation.R
import com.we.presentation.base.BaseActivity
import com.we.presentation.component.ShareData
import com.we.presentation.databinding.ActivitySignBinding
import com.we.presentation.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SignActivity : BaseActivity<ActivitySignBinding>(R.layout.activity_sign) {
    private lateinit var navController: NavController
    private val mainViewModel : MainViewModel by viewModels()

    override fun init() {
        setNavGraph()
        getFcmToken()
    }

    private fun setNavGraph() {
        val type = intent.getBooleanExtra("type", true)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_sign) as NavHostFragment
        navController = navHostFragment.navController
        navController.navInflater.inflate(R.navigation.sign_nav_graph).apply {
            val id = when (ShareData.transferType) {
                true -> {
                    if (type) {
                        R.id.signInFragment
                    } else {
                        R.id.signUpFragment
                    }
                }

                else -> {
                    R.id.signInFragment
                }
            }
            setStartDestination(id)
            navController.setGraph(this, null)
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
package com.we.presentation.home

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.we.presentation.R
import com.we.presentation.base.BaseActivity
import com.we.presentation.databinding.ActivityInvitationDetailBinding
import com.we.presentation.home.viewmodel.InvitationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class InvitationDetailActivity :
    BaseActivity<ActivityInvitationDetailBinding>(R.layout.activity_invitation_detail) {
    private val invitationViewModel: InvitationViewModel by viewModels()


    override fun init() {
        initData()
        initWebView()
    }

    private fun initData() {
        if (Intent.ACTION_VIEW == intent.action) {
            val uri = intent.data
            if (uri != null) {
                val id = uri.getQueryParameter("invitationId")
                invitationViewModel.setInvitationId(id?.toInt() ?: 0)
            }
        } else {
            val number = intent.getIntExtra("param", 0)
            if (number != 0) {
                invitationViewModel.setInvitationId(number)
            }
        }
    }

    private fun initWebView() {
        invitationViewModel.invitationId.flowWithLifecycle(lifecycle)
            .onEach {
                if (it != 0) {
                    binding.webView.apply {
                        clearCache(true)
                        settings.javaScriptEnabled = true
                        getSettings().apply {
                            setLoadWithOverviewMode(true);
                            userAgentString = ("WE/1.0")
                        }
                        settings.domStorageEnabled = true
                        loadUrl("https://j11d104.p.ssafy.io/invitation/storage/$it")
                    }
                }

            }
            .launchIn(lifecycleScope)
    }
}
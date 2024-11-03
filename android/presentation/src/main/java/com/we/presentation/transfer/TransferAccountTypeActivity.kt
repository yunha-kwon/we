package com.we.presentation.transfer

import android.content.Intent
import com.we.presentation.R
import com.we.presentation.base.BaseActivity
import com.we.presentation.component.ShareData
import com.we.presentation.databinding.ActivityTransferAccountTypeBinding
import com.we.presentation.sign.SignActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TransferAccountTypeActivity :
    BaseActivity<ActivityTransferAccountTypeBinding>(R.layout.activity_transfer_account_type) {

    override fun init() {
        initClickEvent()
        initData()
    }

    private fun initClickEvent() {
        val intent = Intent(this, SignActivity::class.java)
        binding.apply {
            tvYes.setOnClickListener {
                intent.putExtra("type", true)
                startActivity(intent)
                finish()
            }
            tvNo.setOnClickListener {
                intent.putExtra("type", false)
                startActivity(intent)
                finish()
            }

        }
    }

    private fun initData() {
        if (Intent.ACTION_VIEW == intent.action) {
            val uri = intent.data
            if (uri != null) {
                ShareData.transferType = true
                val id = uri.getQueryParameter("id")?.toInt() ?: -1
                ShareData.legId = id
                Timber.tag("이체하기").d("$id")
            }
        }
    }
}